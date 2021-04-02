package io.github.h4j4x.codegen.lib.internal.error;

/**
 * Templates related errors.
 */
public class TemplateError extends Exception {
    /**
     * Create a template exception.
     * @param message the error message.
     */
    public TemplateError(String message) {
        super(message);
    }

    /**
     * Create a template exception.
     * @param message the error message.
     * @param cause the parent exception.
     */
    public TemplateError(String message, Throwable cause) {
        super(message, cause);
    }
}
