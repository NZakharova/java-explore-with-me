package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends EwmException {
    public ConflictException(String message) {
        super(message, "For the requested operation the conditions are not met.", HttpStatus.CONFLICT);
    }
}
