package xyz.catuns.spring.base.exception.controller;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ControllerException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
