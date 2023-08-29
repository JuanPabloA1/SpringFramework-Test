package com.api.rest.empleado;

public class ResourceNotFoundException  extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
