package xyz.catuns.spring.base.exception.controller;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ControllerException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
