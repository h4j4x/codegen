package io.github.ajax.codegen.lib.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonParserTests {
    @Test
    public void testParseMap() throws JsonProcessingException {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("{\"%s\": \"%s\"}", foo, bar);
        Map map = JsonParser.parseString(json, Map.class);
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.containsKey(foo));
        Assertions.assertEquals(bar, map.get(foo));
    }
}
