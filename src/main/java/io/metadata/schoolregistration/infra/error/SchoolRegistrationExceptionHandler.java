package io.metadata.schoolregistration.infra.error;

import io.metadata.schoolregistration.infra.error.exception.BadRequestException;
import io.metadata.schoolregistration.infra.error.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class SchoolRegistrationExceptionHandler {

    private static final String CLIENT_SIDE_ERROR_LOG_MESSAGE = "Client side error";
    private static final String ERROR_MESSAGE_LOG_KEY = "error_message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn(CLIENT_SIDE_ERROR_LOG_MESSAGE, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn(CLIENT_SIDE_ERROR_LOG_MESSAGE, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_MESSAGE_LOG_KEY, ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(BadRequestException ex) {
        log.warn(CLIENT_SIDE_ERROR_LOG_MESSAGE, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR_MESSAGE_LOG_KEY, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(RuntimeException ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(ERROR_MESSAGE_LOG_KEY, "Sorry an unexpected error occurred. =("));
    }
}
