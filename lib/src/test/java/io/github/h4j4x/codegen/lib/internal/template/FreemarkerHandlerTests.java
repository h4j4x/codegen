package io.github.h4j4x.codegen.lib.internal.template;

import io.github.h4j4x.codegen.lib.internal.error.TemplateError;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FreemarkerHandlerTests {
    @Test
    public void testRenderTemplate() throws URISyntaxException, IOException, TemplateError {
        URL templatesDirUrl = getClass().getResource("/templates");
        File templatesDir = new File(templatesDirUrl.toURI());
        FreemarkerHandler handler = new FreemarkerHandler(templatesDir);

        String name = "CodeGen";
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        String expected = String.format("Hello %s.", name);
        String result = handler.render("template1", data)
            .replaceAll("\r", "")
            .replaceAll("\n", "");
        Assertions.assertEquals(expected, result);
    }
}
