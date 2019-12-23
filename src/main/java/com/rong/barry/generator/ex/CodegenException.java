package com.rong.barry.generator.ex;

/**
 * 
 * @author quansr
 *
 */
public class CodegenException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CodegenException(String message) {
        super(message);
    }

    public CodegenException(String message, Throwable cause) {
        super(message, cause);
    }
}
