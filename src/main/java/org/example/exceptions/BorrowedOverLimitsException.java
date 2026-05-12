package org.example.exceptions;

public class BorrowedOverLimitsException extends RuntimeException {
    public BorrowedOverLimitsException(String message) {
        super(message);
    }
}
