package com.memsource.backend.memsource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Token cannot be retrieved for user configuration")
public class AuthTokenRetrievalException extends RuntimeException {
    public AuthTokenRetrievalException(String message){
        super(message);
    }
}
