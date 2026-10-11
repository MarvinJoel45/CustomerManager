package com.debuggeandoideas.customer_manager.exceptions;

/**
 * Excepcion para los reintentos
 */
public class RetryableStreamException extends RuntimeException {
    public RetryableStreamException(String message) {
        super(message);
    }
}
