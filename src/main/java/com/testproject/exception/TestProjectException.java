package com.testproject.exception;

public class TestProjectException extends RuntimeException {

    public TestProjectException() {
    }

    public TestProjectException(String message) {
        super(message);
    }

    public TestProjectException(String message, Throwable cause) {
        super(message, cause);
    }

}
