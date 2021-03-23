package io.github.h4j4x.codegen.lib.error;

public class TemplateError extends Exception {
    public TemplateError(String message) {
        super(message);
    }

    public TemplateError(String message, Throwable cause) {
        super(message, cause);
    }
}
