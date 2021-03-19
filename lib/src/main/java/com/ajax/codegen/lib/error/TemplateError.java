package com.ajax.codegen.lib.error;

public class TemplateError extends Exception {
    public TemplateError(String message) {
        super(message);
    }

    public TemplateError(String message, Throwable cause) {
        super(message, cause);
    }
}
