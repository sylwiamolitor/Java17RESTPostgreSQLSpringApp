package com.example.exception;

import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ParseException.class)
    public void handleException(ParseException e) {
        logger.error("An error occurred: ", e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleCustomNotFoundException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleCustomDuplicateException(IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.IM_USED);
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> handleCustomPropertyException(PropertyValueException ex) {
        return new ResponseEntity<>("Missing field " + ex.getPropertyName(), HttpStatus.BAD_REQUEST);
    }
}