package com.testproject.exception;

public class OrderNotFoundException extends TestProjectException {

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
