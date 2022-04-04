package com.memsource.backend.memsource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "AppConfiguration already exists")
public class UserConfigurationAlreadyPresentException extends RuntimeException {
    public UserConfigurationAlreadyPresentException(String message) {
        super(message);
    }
}
