package br.com.gustavo.demo.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.gustavo.demo.exception.EmailUniqueValidationException;
import br.com.gustavo.demo.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandler {
    
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class) 
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity  
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(EmailUniqueValidationException.class) 
    public ResponseEntity<ErrorMessage> emailUniqueValidationException(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity  
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }
    
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result) {
        return ResponseEntity  
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage(), result));
    }

}
