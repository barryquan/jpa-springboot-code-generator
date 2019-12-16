package com.rong.barry.generator.ex;

/**
 * exception when app running
 *
 * @author gaochen
 * Created on 2019/6/21.
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
