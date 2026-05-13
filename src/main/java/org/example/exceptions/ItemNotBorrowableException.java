package org.example.exceptions;

public class ItemNotBorrowableException extends RuntimeException {
    public ItemNotBorrowableException(String message) {
        super(message);
    }
}
