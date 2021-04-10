package io.github.h4j4x.codegen.lib.internal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * JSON utils.
 */
public class JsonUtils {
    /**
     * Parse a json file input.
     * @param <T> the target type.
     * @param file the csv file input.
     * @param toClass the target class.
     * @return the object.
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T parseFile(File file, Class<T> toClass) throws IOException {
        String json = FileUtils.readString(file);
        return parseString(json, toClass);
    }

    /**
     * Parse a json string input.
     * @param <T> the target type.
     * @param json the json string input.
     * @param toClass the target class.
     * @return the object.
     * @throws JsonProcessingException if an I/O error occurs.
     */
    public static <T> T parseString(String json, Class<T> toClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, toClass);
    }
}
