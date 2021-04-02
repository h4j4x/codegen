package io.github.h4j4x.codegen.lib.internal.parser;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CsvParserTests {
    @Test
    public void testParseLine() {
        String[] values = new String [] { "part0", ",part, 1,", "part2", "part, 3", "part, 4", "part5", "part6" };
        String line = String.format("%s,\"%s\",%s,\"%s\",\"%s\",%s,%s",
            values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
        List<String> parts = CsvParser.parseLine(line);
        Assertions.assertNotNull(parts);
        Assertions.assertEquals(values.length, parts.size());
        for (int i = 0; i < values.length; i++) {
            Assertions.assertEquals(values[i], parts.get(i));
        }
    }
}
