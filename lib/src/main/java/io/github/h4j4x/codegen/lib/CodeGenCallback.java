package io.github.h4j4x.codegen.lib;

public interface CodeGenCallback {
    void logInfo(String message);

    void logWarning(String message);

    void logError(String message);
}
