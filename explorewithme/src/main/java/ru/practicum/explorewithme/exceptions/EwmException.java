package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;
import ru.practicum.explorewithme.utils.DateUtils;

import java.time.LocalDateTime;

public class EwmException extends RuntimeException {
    private final String reason;
    private final LocalDateTime timestamp;
    private final HttpStatus status;

    public EwmException(String message, String reason, HttpStatus status, LocalDateTime timestamp) {
        super(message);
        this.reason = reason;
        this.timestamp = timestamp;
        this.status = status;
    }

    public EwmException(String message, String reason, HttpStatus status) {
        this(message, reason, status, DateUtils.now());
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
