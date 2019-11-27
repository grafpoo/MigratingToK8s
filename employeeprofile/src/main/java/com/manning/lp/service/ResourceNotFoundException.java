package com.manning.lp.service;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super("No Employee with ID: "+id);
    }

}
