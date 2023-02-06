package ru.practicum.shareit.exceptions;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class RequestError extends ResponseStatusException {

    public RequestError(HttpStatus status, String message) {
        super(status, message);
    }

    public String getError() {
        return "";
    }

    public String getDescription() {
        return "";
    }
}