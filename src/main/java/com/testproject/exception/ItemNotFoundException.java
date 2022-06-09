package com.testproject.exception;

public class ItemNotFoundException extends TestProjectException {

    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
