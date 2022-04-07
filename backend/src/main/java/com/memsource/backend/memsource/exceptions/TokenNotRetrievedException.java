package com.memsource.backend.memsource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Auth token not retrieved")
public class TokenNotRetrievedException extends RuntimeException {
    public TokenNotRetrievedException(String message, Throwable t) {
        super(message, t);
    }
}
