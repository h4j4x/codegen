package com.ajax.codegen.app;

import com.ajax.codegen.app.model.DataInput;
import com.ajax.codegen.app.model.TemplateObject;
import com.ajax.codegen.lib.json.JsonParser;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CliAppTests {
    @TempDir
    Path temp;

    @Test
    public void testGeneration() throws URISyntaxException, IOException {
        File output = temp.toFile();
        File dataDir = getResourceFilePath("data");
        String[] args = {
            "-d", dataDir.getAbsolutePath(),
            "-t", getResourceFilePath("templates").getAbsolutePath(),
            "-o", output.getAbsolutePath(),
        };
        CliApp.main(args);
        File[] files = output.listFiles();
        Assertions.assertNotNull(files);
        Assertions.assertTrue(files.length > 0);

        DataInput testData = JsonParser.parseFile(new File(dataDir, "test.json"), DataInput.class);
        for (TemplateObject templateObject : testData.getTemplates()) {
            File file = new File(output, templateObject.getFile());
            Assertions.assertTrue(file.exists());
            Assertions.assertTrue(file.isFile());
            Assertions.assertTrue(file.length() > 0);
        }
    }

    private File getResourceFilePath(String folder) throws URISyntaxException {
        URL dirUrl = getClass().getResource("/" + folder);
        return new File(dirUrl.toURI());
    }
}
