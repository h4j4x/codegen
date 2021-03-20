package com.ajax.codegen.lib.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JsonParser {
    public static <T> T parseString(String json, Class<T> toClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, toClass);
    }

    public static <T> T parseFile(File jsonFile, Class<T> toClass) throws IOException {
        String json = Files.readString(jsonFile.toPath());
        return parseString(json, toClass);
    }
}
