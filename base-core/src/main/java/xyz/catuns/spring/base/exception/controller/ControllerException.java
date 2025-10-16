package xyz.catuns.spring.base.exception.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ControllerException extends RuntimeException {

    protected final HttpStatus httpStatus;
    protected final String title;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ControllerException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ControllerException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, httpStatus.getReasonPhrase());
    }

    public ControllerException(String message, HttpStatus httpStatus, String title) {
        super(message);
        this.httpStatus = httpStatus;
        this.title = title;
    }

}
