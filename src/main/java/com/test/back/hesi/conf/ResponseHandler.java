package com.test.back.hesi.conf;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class ResponseHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        Map<String, Object> exceptionResponse = new HashMap<String, Object>();
        exceptionResponse.put("result", false);
        exceptionResponse.put("msg", ex.getMessage());
        exceptionResponse.put("time", new Date());
        exceptionResponse.put("details", request.getDescription(false));
        
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(HttpServerErrorException.class)
    public final ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request){
        Map<String, Object> exceptionResponse = new HashMap<String, Object>();
        exceptionResponse.put("result", false);
        exceptionResponse.put("msg", ex.getMessage());
        exceptionResponse.put("code", ex.getStatusCode());
        
        return new ResponseEntity(exceptionResponse, ex.getStatusCode());
    }
}
