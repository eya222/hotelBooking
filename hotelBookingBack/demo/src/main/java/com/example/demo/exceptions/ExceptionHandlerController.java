package com.example.demo.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DuplicateRoomException.class)
    //@ResponseStatus(HttpStatus.CONFLICT)
    //@ResponseBody
    public ResponseEntity<ErrorResponse> handleDuplicateContactException(DuplicateRoomException e, WebRequest request){
        final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(Instant.now())
        .error("Duplicate Room")
        .status(HttpStatus.CONFLICT.value())
        .message(e.getMessage())
        .path(request.getDescription(false))
        .build();
     return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(DuplicateCustomerException.class)
    //@ResponseStatus(HttpStatus.CONFLICT)
    //@ResponseBody
    public ResponseEntity<ErrorResponse> handleDuplicateCustomerException(DuplicateCustomerException e, WebRequest request){
        final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(Instant.now())
        .error("Duplicate Customer")
        .status(HttpStatus.CONFLICT.value())
        .message(e.getMessage())
        .path(request.getDescription(false))
        .build();
     return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    
    @ExceptionHandler(EntityNotFoundException.class)
    //@ResponseStatus(HttpStatus.NOT_FOUND)
   // @ResponseBody 
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request){
        final ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(Instant.now())
        .error("Invalid ID")
        .status(HttpStatus.NOT_FOUND.value())
        .message(e.getMessage())
        .path(request.getDescription(false))
        .build();
     return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    } 
//Any Other exception
    @ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //@ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request)    {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .error("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
