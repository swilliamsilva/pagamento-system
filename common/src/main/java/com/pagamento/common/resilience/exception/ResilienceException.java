package com.pagamento.common.resilience.exception;

/**
 * Exceção lançada quando todas as tentativas de resiliência falham
 */
public class ResilienceException extends RuntimeException {
    
    public ResilienceException(String message) {
        super(message);
    }

    public ResilienceException(String message, Throwable cause) {
        super(message, cause);
    }
}