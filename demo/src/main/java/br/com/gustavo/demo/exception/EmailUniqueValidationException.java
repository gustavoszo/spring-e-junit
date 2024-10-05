package br.com.gustavo.demo.exception;

public class EmailUniqueValidationException extends RuntimeException {
    
    public EmailUniqueValidationException(String msg) {
        super(msg);
    }

}
