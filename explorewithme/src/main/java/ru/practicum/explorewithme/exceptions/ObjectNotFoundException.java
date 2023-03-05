package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends EwmException {
    public ObjectNotFoundException(String objectType, long id) {
        super(getMessage(objectType, id), "The required object was not found.", HttpStatus.NOT_FOUND);
    }

    private static String getMessage(String objectType, long id) {
        return objectType + " with id=" + id + " was not found";
    }
}
