package xyz.catuns.spring.base.exception.controller;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ControllerException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
