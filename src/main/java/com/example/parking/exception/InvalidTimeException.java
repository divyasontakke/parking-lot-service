package com.example.parking.exception;

/** Logically invalid timestamps (exit before entry, entry in the future), mapped to HTTP 400. */
public class InvalidTimeException extends RuntimeException {
    public InvalidTimeException(String message) { super(message); }
}
