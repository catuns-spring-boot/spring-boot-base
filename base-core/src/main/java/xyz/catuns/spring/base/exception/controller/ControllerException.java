package xyz.catuns.spring.base.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class ControllerException extends ErrorResponseException {

    /**
     * todo: Turns out i may not need this class anymore since theres
     * ResponseStatusException -> and parents
     */
    public ControllerException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ControllerException(String reason) {
        this(reason, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ControllerException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public ControllerException(String reason, HttpStatus httpStatus) {
        super(httpStatus);
        this.setDetail(reason);

    }

}
