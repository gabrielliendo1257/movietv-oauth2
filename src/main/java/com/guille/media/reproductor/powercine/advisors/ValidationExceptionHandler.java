package com.guille.media.reproductor.powercine.advisors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationErrors(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach((fieldError) ->
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage())
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> validationErrors(ConstraintViolationException ex)
    {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(constraintViolation ->
                        errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()
                        )
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
