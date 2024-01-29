package com.toyproject.sh.exception;

public class ExceptionHandler extends RuntimeException{

    public ExceptionHandler (String message){
        super(message);
    }
    public static class DuplicateEmailException extends ExceptionHandler {
        public DuplicateEmailException(String message) {
            super(message);
        }
    }
}
