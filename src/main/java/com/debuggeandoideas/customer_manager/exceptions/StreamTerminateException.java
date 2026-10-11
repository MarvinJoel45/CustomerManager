package com.debuggeandoideas.customer_manager.exceptions;

/**
 * Excepcion para los terminar el flujo en caso de errores fatales
 */
public class StreamTerminateException extends RuntimeException {
    public StreamTerminateException(String message) {
        super(message);
    }
}
