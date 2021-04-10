package io.github.h4j4x.codegen.lib.internal.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.h4j4x.codegen.lib.internal.util.JsonUtils;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonUtilsTests {
    @Test
    public void testParseMap() throws JsonProcessingException {
        String foo = "foo";
        String bar = "bar";
        String json = String.format("{\"%s\": \"%s\"}", foo, bar);
        Map map = JsonUtils.parseString(json, Map.class);
        assertNotNull(map);
        assertTrue(map.containsKey(foo));
        assertEquals(bar, map.get(foo));
    }
}
