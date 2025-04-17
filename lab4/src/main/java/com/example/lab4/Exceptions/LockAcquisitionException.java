package com.example.lab4.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LockAcquisitionException extends RuntimeException {
    
    public LockAcquisitionException() {
        super("Could not acquire lock. Another process is using this resource.");
    }
    
    public LockAcquisitionException(String message) {
        super(message);
    }
    
    public LockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
