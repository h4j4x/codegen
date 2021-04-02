package io.github.h4j4x.codegen.lib.internal.parser;

import io.github.h4j4x.codegen.lib.internal.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Csv format parser.
 */
public class CsvParser {
    private static final String DELIMITER = ",";
    private static final String WRAPPER = "\"";

    /**
     * Parse a csv file input.
     * @param file the csv file input.
     * @return list of data.
     * @throws IOException if an I/O error occurs.
     */
    public static List<List<String>> parseFile(File file) throws IOException {
        String csv = FileUtils.readString(file);
        return parseString(csv);
    }

    /**
     * Parse a csv string input.
     * @param csv the csv string input.
     * @return list of data.
     */
    public static List<List<String>> parseString(String csv) {
        List<List<String>> list = new LinkedList<>();
        if (csv != null) {
            String[] lines = csv.split("\n");
            for (String line : lines) {
                List<String> parsed = parseLine(line);
                if (!parsed.isEmpty()) {
                    list.add(parsed);
                }
            }
        }
        return list;
    }

    /**
     * Parse a csv string row.
     * @param line the csv string row.
     * @return list of values.
     */
    public static List<String> parseLine(String line) {
        if (line != null) {
            List<String> parts = new LinkedList<>();
            int sepIndex = line.indexOf(DELIMITER);
            int fromIndex = sepIndex + 1;
            int lastIndex = -1;
            while (sepIndex > 0) {
                int escapeStartIndex = line.indexOf(WRAPPER, lastIndex + 1);
                int escapeEndIndex = line.indexOf(WRAPPER, escapeStartIndex + 1);
                if (sepIndex < escapeStartIndex || sepIndex > escapeEndIndex) {
                    parts.add(cleanWrappers(line.substring(lastIndex + 1, sepIndex)));
                    lastIndex = sepIndex;
                } else {
                    fromIndex = escapeEndIndex;
                }
                sepIndex = line.indexOf(DELIMITER, fromIndex);
                fromIndex = sepIndex + 1;
            }
            if (lastIndex > 0) {
                parts.add(cleanWrappers(line.substring(lastIndex + 1)));
            }
            return parts;
        }
        return Collections.emptyList();
    }

    private static String cleanWrappers(String value) {
        if (value != null) {
            String cleaned = value;
            while (cleaned.startsWith(WRAPPER)) {
                cleaned = cleaned.substring(WRAPPER.length());
            }
            while (cleaned.endsWith(WRAPPER)) {
                cleaned = cleaned.substring(0, cleaned.length() - WRAPPER.length());
            }
            return cleaned;
        }
        return null;
    }
}
