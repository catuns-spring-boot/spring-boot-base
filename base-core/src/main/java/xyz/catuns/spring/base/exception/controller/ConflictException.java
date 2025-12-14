package xyz.catuns.spring.base.exception.controller;

import org.springframework.http.HttpStatus;

public class ConflictException extends ControllerException {

    public ConflictException() {
        super();
    }

    public ConflictException(String reason) {
        super(reason, HttpStatus.CONFLICT);
    }
}
