package io.github.h4j4x.codegen.lib;

import io.github.h4j4x.codegen.lib.model.DataInput;
import io.github.h4j4x.codegen.lib.model.TemplateObject;
import io.github.h4j4x.codegen.lib.parser.JsonParser;
import io.github.h4j4x.codegen.lib.util.FileUtils;
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

public class CodeGenTests {
    @TempDir
    Path temp;

    @Test
    public void testGeneration() throws URISyntaxException, IOException {
        File dataFolder = getResourceFilePath("data");
        File templatesFolder = getResourceFilePath("templates");
        File output = temp.toFile();
        CodeGen codeGen = new CodeGen(dataFolder, templatesFolder, output, false, false);
        codeGen.generateCode(new SilentCodeGenCallback());
        List<File> files = FileUtils.readFiles(output, File::isFile, true);
        Assertions.assertFalse(files.isEmpty());

        DataInput testData = JsonParser.parseFile(new File(dataFolder, "test.json"), DataInput.class);
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

    private static class SilentCodeGenCallback implements CodeGenCallback {
        @Override
        public void logInfo(String message) {}

        @Override
        public void logWarning(String message) {}

        @Override
        public void logError(String message) {}
    }
}
