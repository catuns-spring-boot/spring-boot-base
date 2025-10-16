package xyz.catuns.spring.base.exception.controller;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ControllerException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
