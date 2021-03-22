package io.github.ajax.codegen.app;

import io.github.ajax.codegen.app.model.DataInput;
import io.github.ajax.codegen.app.model.TemplateObject;
import io.github.ajax.codegen.lib.json.JsonParser;
import io.github.ajax.codegen.lib.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
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
        List<File> files = FileUtils.readFiles(output, File::isFile, true);
        Assertions.assertFalse(files.isEmpty());

        DataInput testData = JsonParser.parseFile(new File(dataDir, "test.json"), DataInput.class);
        List<String> merges = new LinkedList<>();
        for (TemplateObject templateObject : testData.getTemplates()) {
            if (templateObject.hasFile()) {
                File file = FileUtils.getFile(output, templateObject.getFile());
                Assertions.assertTrue(file.exists());
                Assertions.assertTrue(file.isFile());
                Assertions.assertTrue(file.length() > 0);
                String content = FileUtils.readString(file);
                Assertions.assertTrue(content.contains("\"tests\""));
            }
            if (templateObject.hasMerge()
                && !merges.contains(templateObject.getMergeInFile())) {
                merges.add(templateObject.getMergeInFile());
            }
        }
        for (String merge : merges) {
            File file = FileUtils.getFile(output, merge);
            Assertions.assertTrue(file.exists());
            Assertions.assertTrue(file.isFile());
            Assertions.assertTrue(file.length() > 0);
            String content = Files.readString(file.toPath());
            Assertions.assertTrue(content.contains("\"tests\""));
        }
    }

    private File getResourceFilePath(String folder) throws URISyntaxException {
        URL dirUrl = getClass().getResource("/" + folder);
        return new File(dirUrl.toURI());
    }
}
