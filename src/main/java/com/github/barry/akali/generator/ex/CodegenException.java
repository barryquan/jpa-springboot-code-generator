package com.github.barry.akali.generator.ex;

/**
 * 代码生成器的异常
 * 
 * @author barry
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
