package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends EwmException {
    public InvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
    }
}
