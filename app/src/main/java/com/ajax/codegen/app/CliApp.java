package com.ajax.codegen.app;

import com.ajax.codegen.app.model.DataInput;
import com.ajax.codegen.app.model.MergeData;
import com.ajax.codegen.app.model.MergeObject;
import com.ajax.codegen.app.model.TemplateObject;
import com.ajax.codegen.lib.error.TemplateError;
import com.ajax.codegen.lib.json.JsonParser;
import com.ajax.codegen.lib.template.FreemarkerHandler;
import com.ajax.codegen.lib.util.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.Messages;

public class CliApp {
    private static final Logger log = Logger.getLogger(CliApp.class.getName());

    @Option(name="-d", usage="Data folder", required = true)
    private File dataFolder;

    @Option(name="-t", usage="Templates folder", required = true)
    private File templatesFolder;

    @Option(name="-o", usage="Output folder", required = true)
    private File outFolder;

    @Option(name="--verbose", usage="Verbose mode")
    @SuppressWarnings("FieldMayBeFinal")
    private boolean verbose = false;

    @Option(name="--overwrite", usage="Overwrite mode")
    @SuppressWarnings("FieldMayBeFinal")
    private boolean overwrite = false;

    @Option(name="--read-recursive", usage="Deep data folder read mode")
    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    private boolean readRecursive = false;

    public static void main(String[] args) {
        new CliApp().doMain(args);
    }

    private void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (!templatesFolder.canRead()) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, "Templates folder is unreadable.");
            }
            if (!templatesFolder.isDirectory()) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, "Templates folder is not a folder.");
            }
            if (!dataFolder.canRead()) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, "Data folder is unreadable.");
            }
            if (!dataFolder.isDirectory()) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, "Data folder is not a folder.");
            }
            if (!outFolder.canWrite()) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, "Output folder cannot be written.");
            }
            if (!outFolder.isDirectory()) {
                throw new CmdLineException(parser, Messages.ILLEGAL_PATH, "Output folder is not a folder.");
            }
            generateFiles();
        } catch(CmdLineException e) {
            logError(e.getMessage());
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            parser.printUsage(outStream);
            logError(String.format("Usage:\n%s", outStream.toString(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    private void generateFiles() throws IOException {
        if (overwrite) {
            logInfo("Overwrite mode is ON (Existing files will be replaced).");
        } else {
            logInfo("Overwrite mode is OFF (Existing files will be skipped).");
        }
        logInfo(String.format("Reading templates from %s...", templatesFolder.getName()));
        FreemarkerHandler templateHandler = new FreemarkerHandler(templatesFolder);

        logInfo(String.format("Reading data from %s...", dataFolder.getName()));
        List<File> jsonFiles = FileUtils.readFiles(
            dataFolder, file -> file.isFile() && file.getName().endsWith(".json"), readRecursive);
        if (jsonFiles.size() > 0) {
            Map<String, MergeData> merges = new HashMap<>();
            for (File jsonFile : jsonFiles) {
                logInfo(String.format(" - Processing %s...", jsonFile.getName()));
                try {
                    DataInput dataInput = JsonParser.parseFile(jsonFile, DataInput.class);
                    generateFiles(templateHandler, dataInput, merges);
                } catch (IOException e) {
                    logError(String.format(" - Error reading %s: %s", jsonFile.getName(), e.getMessage()));
                }
            }
            merges.values().forEach(mergeData -> {
                if (mergeData.isValid()) {
                    logInfo(String.format(" - Processing merge %s...", mergeData.getFile()));
                    try {
                        generateMerge(templateHandler, mergeData);
                    } catch (IOException | TemplateError e) {
                        logError(String.format(" - Error processing merge %s: %s", mergeData.getFile(), e.getMessage()));
                    }
                }
            });
        } else {
            logWarning(String.format("Nothing to process at %s!", dataFolder.getName()));
        }
    }

    private void generateFiles(FreemarkerHandler templateHandler, DataInput dataInput, Map<String, MergeData> merges) {
        Object data = dataInput.getData();
        if (data != null) {
            dataInput.getTemplates().forEach(templateObj -> {
                if (templateObj.hasFile()) {
                    generateFile(templateHandler, templateObj, data);
                } else if (templateObj.hasMerge()) {
                    String template = templateObj.getMergeInTemplate();
                    String file = templateObj.getMergeInFile();
                    String mergeKey = String.format("%s-%s", template, file);
                    MergeData mergeData = merges.getOrDefault(mergeKey, new MergeData(template, file));
                    mergeData.addObject(new MergeObject(templateObj.getTemplate(), data));
                    merges.put(mergeKey, mergeData);
                }
            });
        }
    }

    private void generateFile(FreemarkerHandler templateHandler, TemplateObject templateObject, Object data) {
        try {
            String file = templateObject.getFile();
            logInfo(String.format("   - Creating %s...", file));
            if (createFile(file, templateHandler, templateObject.getTemplate(), data)) {
                logInfo(String.format("   - %s successfully created!", file));
            } else {
                logInfo(String.format("   - %s already exists. Skipped.", file));
            }
        } catch (IOException | TemplateError e) {
            logError("   - Error: " + e.getMessage());
        }
    }

    private void generateMerge(FreemarkerHandler templateHandler, MergeData mergeData) throws IOException, TemplateError {
        String file = mergeData.getFile();
        logInfo(String.format("   - Creating merge %s...", file));
        Map<String, String> data = new HashMap<>();
        data.put("content", mergeContent(templateHandler, mergeData.getObjects()));
        if (createFile(file, templateHandler, mergeData.getTemplate(), data)) {
            logInfo(String.format("   - %s successfully created!", file));
        } else {
            logInfo(String.format("   - %s already exists. Skipped.", file));
        }
    }

    private boolean createFile(String path, FreemarkerHandler templateHandler, String template, Object data) throws IOException, TemplateError {
        File file = FileUtils.getFile(outFolder, path);
        if (file.exists()) {
            if (!overwrite) {
                return false;
            }
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            //noinspection ResultOfMethodCallIgnored
            parentFile.mkdirs();
        }
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        String content = templateHandler.render(template, data);
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
        return true;
    }

    private String mergeContent(FreemarkerHandler templateHandler, List<MergeObject> mergeObjects) throws IOException, TemplateError {
        StringBuilder content = new StringBuilder();
        for (MergeObject mergeObject : mergeObjects) {
            if (content.length() > 0) {
                content.append("\n");
            }
            String templateContent = templateHandler.render(mergeObject.getTemplate(), mergeObject.getData());
            content.append(templateContent);
        }
        return content.toString();
    }

    private void logInfo(String message) {
        if (verbose) {
            log.log(Level.INFO, message);
        }
    }

    private void logWarning(String message) {
        if (verbose) {
            log.log(Level.WARNING, message);
        }
    }

    private void logError(String message) {
        if (verbose) {
            log.log(Level.SEVERE, message);
        }
    }
}
