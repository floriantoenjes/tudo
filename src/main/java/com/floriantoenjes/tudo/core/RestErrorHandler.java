package com.floriantoenjes.tudo.core;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class RestErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RepositoryConstraintViolationException.class)
    public ResponseEntity<?> processValidationError(RepositoryConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getErrors().getGlobalError(), HttpStatus.BAD_REQUEST);
    }
}
