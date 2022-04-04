package com.memsource.backend.memsource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "AppConfiguration not present")
public class UserConfigurationNotPresent extends RuntimeException {
    public UserConfigurationNotPresent(String message) {
        super(message);
    }
}
