package io.github.h4j4x.codegen.common.parser;

import io.github.h4j4x.codegen.common.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CsvParser {
    public static List<List<String>> parseString(String csv) {
        List<List<String>> list = new LinkedList<>();
        if (csv != null) {
            String[] lines = csv.split("\n");
            for (String line : lines) {
                if (!line.isBlank()) {
                    list.add(Arrays.asList(line.split(",")));
                }
            }
        }
        return list;
    }

    public static List<List<String>> parseFile(File jsonFile) throws IOException {
        String csv = FileUtils.readString(jsonFile);
        return parseString(csv);
    }
}
