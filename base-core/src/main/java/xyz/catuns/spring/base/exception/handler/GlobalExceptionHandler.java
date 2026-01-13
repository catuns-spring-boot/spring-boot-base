package xyz.catuns.spring.base.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.catuns.spring.base.exception.controller.ControllerException;
import xyz.catuns.spring.base.properties.ExceptionHandlerMetadata;

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

    private final ExceptionHandlerMetadata properties;

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

        e.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity
                .status(e.getStatusCode())
                .headers(e.getHeaders())
                .body(e.getBody());
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

        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        if (properties.isIncludeBindingErrors()) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }

}
