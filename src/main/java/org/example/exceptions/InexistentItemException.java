package org.example.exceptions;

public class InexistentItemException extends RuntimeException {
    public InexistentItemException(String message) {
        super(message);
    }
}
