package xyz.catuns.spring.base.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.catuns.spring.base.exception.controller.ControllerException;
import xyz.catuns.spring.base.properties.ExceptionHandlerProperties;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ExceptionHandlerProperties properties;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> genericExceptionHandler(
            Exception e,
            HttpServletRequest request
    ) {
        if (properties.isLogExceptions()) {
            log.error("[{}] {} [path {}]",e.getClass().getSimpleName(), e.getMessage(), request.getRequestURI());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );

        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setTitle(e.getClass().getSimpleName());

        if (properties.isIncludeStackTrace()) {
            problemDetail.setProperty("stackTrace",
                    Arrays.stream(e.getStackTrace())
                            .limit(10)
                            .map(StackTraceElement::toString)
                            .collect(Collectors.toList())
            );
        }

        if (properties.isIncludeCause() && e.getCause() != null) {
            problemDetail.setProperty("cause", e.getCause().getMessage());
        }

        return ResponseEntity
                .status(problemDetail.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ProblemDetail> controllerExceptionHandler(
            ControllerException e,
            HttpServletRequest request
    ) {
        if (properties.isLogExceptions()) {
            log.warn("Controller exception at {}: {}", request.getRequestURI(), e.getMessage());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                e.getHttpStatus(),
                e.getMessage()
        );

        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setTitle(e.getTitle());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(problemDetail);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        if (properties.isLogExceptions()) {
            log.warn("Validation failed at {}", request.getRequestURI());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields"
        );

        problemDetail.setTitle("Bad Request");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());

        if (properties.isIncludeBindingErrors()) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {

        if (properties.isLogExceptions()) {
            log.warn("Constraint violation at {}", request.getRequestURI());
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Constraint violation"
        );

        problemDetail.setTitle("Bad Request");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());

        if (properties.isIncludeBindingErrors()) {
            Map<String, String> errors = new HashMap<>();
            ex.getConstraintViolations().forEach(violation -> {
                String propertyPath = violation.getPropertyPath().toString();
                errors.put(propertyPath, violation.getMessage());
            });
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }
}
