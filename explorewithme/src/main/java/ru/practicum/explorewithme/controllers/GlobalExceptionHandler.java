package ru.practicum.explorewithme.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.explorewithme.dto.ApiError;
import ru.practicum.explorewithme.exceptions.EwmException;
import ru.practicum.explorewithme.utils.DateUtils;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiError> handleEwmException(EwmException exception) {
        return logWarn(exception.getMessage(), exception.getReason(), exception.getStatus(), exception.getTimestamp());
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleThrowable(Throwable throwable) {
        return logError(throwable);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return logWarn(exception);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return logWarn(exception);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        return logWarn(exception);
    }

    private ResponseEntity<ApiError> logWarn(Exception exception) {
        log.warn("Warning", exception);
        return new ResponseEntity<>(new ApiError(null,
                "Bad request",
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.toString(),
                DateUtils.now()
        ), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> logError(Throwable throwable) {
        log.error("Error", throwable);
        return new ResponseEntity<>(new ApiError(null,
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                DateUtils.now()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> logWarn(String message, String reason, HttpStatus status, LocalDateTime timestamp) {
        log.warn("message: " + message + "; reason: " + reason + "; status: " + status + "; timestamp: " + timestamp);
        return new ResponseEntity<>(new ApiError(
                null,
                message,
                reason,
                status.toString(),
                timestamp
        ), status);
    }
}
