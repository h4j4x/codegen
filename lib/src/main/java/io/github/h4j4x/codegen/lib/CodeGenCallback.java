package io.github.h4j4x.codegen.lib;

/**
 * Callback for CodeGen events.
 */
public interface CodeGenCallback {
    /**
     * Info event.
     * @param message the event message.
     */
    void logInfo(String message);

    /**
     * Warning event.
     * @param message the event message.
     */
    void logWarning(String message);

    /**
     * Error event.
     * @param message the event message.
     */
    void logError(String message);
}
