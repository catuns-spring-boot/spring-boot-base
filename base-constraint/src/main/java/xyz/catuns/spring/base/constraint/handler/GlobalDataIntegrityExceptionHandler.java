package xyz.catuns.spring.base.constraint.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.catuns.spring.base.constraint.properties.ConstraintViolationProperties;
import xyz.catuns.spring.base.constraint.parser.ConstraintViolationInfo;
import xyz.catuns.spring.base.constraint.parser.ConstraintViolationParser;

import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalDataIntegrityExceptionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalDataIntegrityExceptionHandler.class);

    private final ConstraintViolationParser parser;
    private final ConstraintViolationProperties properties;


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException e,
            HttpServletRequest request
    ) {
        String rootCause = e.getMostSpecificCause().getMessage();

        log.error("Data integrity violation at {}: {}",
                request.getRequestURI(),
                rootCause,
                e);

        ConstraintViolationInfo violationInfo = parser.parse(rootCause);

        HttpStatus status = HttpStatus.valueOf(properties.getStatusCode());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                violationInfo.getUserFriendlyMessage()
        );

        problemDetail.setTitle("Data Integrity Violation");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("violationType", violationInfo.getType());

        if (violationInfo.getConstraintName() != null) {
            problemDetail.setProperty("constraint", violationInfo.getConstraintName());
        }

        if (violationInfo.getField() != null) {
            problemDetail.setProperty("field", violationInfo.getField());
        }

        // Include rejected values if configured
        if (properties.isIncludeRejectedValues() && violationInfo.getValue() != null) {
            problemDetail.setProperty("rejectedValue", violationInfo.getValue());
        }

        // Expose technical details if configured (for development)
        if (properties.isExposeTechnicalDetails()) {
            problemDetail.setProperty("technicalMessage", rootCause);
        }

        return ResponseEntity
                .status(status)
                .body(problemDetail);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleSQLIntegrityViolation(
            SQLIntegrityConstraintViolationException e,
            HttpServletRequest request
    ) {
        log.error("SQL integrity constraint violation at {}: {}",
                request.getRequestURI(),
                e.getMessage(),
                e);

        // Wrap and delegate to DataIntegrityViolationException handler
        DataIntegrityViolationException wrappedException =
                new DataIntegrityViolationException(e.getMessage(), e);

        return handleDataIntegrityViolation(wrappedException, request);
    }
}
