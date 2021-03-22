package io.github.ajax.codegen.lib.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ajax.codegen.lib.util.FileUtils;
import java.io.File;
import java.io.IOException;

public class JsonParser {
    public static <T> T parseString(String json, Class<T> toClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, toClass);
    }

    public static <T> T parseFile(File jsonFile, Class<T> toClass) throws IOException {
        String json = FileUtils.readString(jsonFile);
        return parseString(json, toClass);
    }
}
