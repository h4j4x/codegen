package com.ajax.codegen.app;

import com.ajax.codegen.app.model.DataInput;
import com.ajax.codegen.lib.json.JsonParser;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class AppTests {
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
        App.main(args);
        File[] files = output.listFiles();
        Assertions.assertNotNull(files);
        Assertions.assertTrue(files.length > 0);

        DataInput testData = JsonParser.parseFile(new File(dataDir, "test.json"), DataInput.class);
        Collection<String> fileNames = testData.getTemplatesFilesMap().values();
        for (String fileName : fileNames) {
            File file = new File(output, fileName);
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
