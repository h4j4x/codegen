package io.github.h4j4x.codegen.lib;

import io.github.h4j4x.codegen.lib.internal.error.TemplateError;
import io.github.h4j4x.codegen.lib.internal.template.FreemarkerHandler;
import io.github.h4j4x.codegen.lib.internal.util.FileUtils;
import io.github.h4j4x.codegen.lib.internal.util.JsonUtils;
import io.github.h4j4x.codegen.lib.model.DataInput;
import io.github.h4j4x.codegen.lib.model.MergeData;
import io.github.h4j4x.codegen.lib.model.MergeObject;
import io.github.h4j4x.codegen.lib.model.TemplateObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CodeGen service.
 */
public class CodeGen {
    private final File dataFolder;
    private final File templatesFolder;
    private final FreemarkerHandler templateHandler;
    private final File outFolder;
    private final boolean overwrite;
    private final boolean readRecursive;

    /**
     * Create CodeGen service.
     * @param dataFolder the folder to read JSON data files.
     * @param templatesFolder the folder to read templates referenced from data files.
     * @param outFolder the folder to output result files.
     * @param overwrite if existing files can be overwritten.
     * @param readRecursive if data folder will be read deeply.
     * @throws IOException if an I/O error occurs.
     */
    public CodeGen(File dataFolder, File templatesFolder, File outFolder,
                   boolean overwrite, boolean readRecursive) throws IOException {
        this.dataFolder = dataFolder;
        this.templatesFolder = templatesFolder;
        templateHandler = new FreemarkerHandler(templatesFolder);
        this.outFolder = outFolder;
        this.overwrite = overwrite;
        this.readRecursive = readRecursive;
    }

    /**
     * Generate output files.
     * @param callback the callback for events.
     */
    public void generateCode(CodeGenCallback callback) {
        if (overwrite) {
            callback.logInfo("Overwrite mode is ON (Existing files will be replaced).");
        } else {
            callback.logInfo("Overwrite mode is OFF (Existing files will be skipped).");
        }
        callback.logInfo(String.format("Reading templates from %s...", templatesFolder.getName()));

        callback.logInfo(String.format("Reading data from %s...", dataFolder.getName()));
        List<File> jsonFiles = FileUtils.readFiles(
            dataFolder, file -> file.isFile() && file.getName().endsWith(".json"), readRecursive);
        if (jsonFiles.size() > 0) {
            Map<String, MergeData> merges = new HashMap<>();
            for (File jsonFile : jsonFiles) {
                callback.logInfo(String.format(" - Processing %s...", jsonFile.getName()));
                try {
                    DataInput dataInput = JsonUtils.parseFile(jsonFile, DataInput.class);
                    generateFiles(templateHandler, dataInput, merges, callback);
                } catch (IOException e) {
                    callback.logError(String.format(" - Error reading %s: %s", jsonFile.getName(), e.getMessage()));
                }
            }
            merges.values().forEach(mergeData -> {
                if (mergeData.isValid()) {
                    callback.logInfo(String.format(" - Processing merge %s...", mergeData.getFile()));
                    try {
                        generateMerge(templateHandler, mergeData, callback);
                    } catch (IOException | TemplateError e) {
                        callback.logError(String.format(" - Error processing merge %s: %s", mergeData.getFile(), e.getMessage()));
                    }
                }
            });
        } else {
            callback.logWarning(String.format("Nothing to process at %s!", dataFolder.getName()));
        }
    }

    private void generateFiles(FreemarkerHandler templateHandler, DataInput dataInput,
                               Map<String, MergeData> merges, CodeGenCallback callback) {
        if (dataInput.getData() != null || dataInput.getCsvData() != null) {
            dataInput.getTemplates().forEach(templateObj -> {
                if (templateObj.hasFile()) {
                    generateFile(templateHandler, templateObj, dataInput, callback);
                } else if (templateObj.hasMerge()) {
                    String template = templateObj.getMergeInTemplate();
                    String file = templateObj.getMergeInFile();
                    String mergeKey = String.format("%s-%s", template, file);
                    MergeData mergeData = merges.getOrDefault(mergeKey, new MergeData(template, file));
                    Object data = dataInput.getData();
                    if (data == null) {
                        data = dataInput.getCsvData();
                    }
                    mergeData.addObject(new MergeObject(templateObj.getTemplate(), data, templateObj.getMergeOrder()));
                    merges.put(mergeKey, mergeData);
                }
            });
        }
    }

    private void generateFile(FreemarkerHandler templateHandler, TemplateObject templateObject,
                              DataInput dataInput, CodeGenCallback callback) {
        try {
            String file = templateObject.getFile();
            callback.logInfo(String.format("   - Creating %s...", file));
            if (createFile(file, templateHandler, templateObject.getTemplate(), dataInput)) {
                callback.logInfo(String.format("   - %s successfully created!", file));
            } else {
                callback.logInfo(String.format("   - %s already exists. Skipped.", file));
            }
        } catch (IOException | TemplateError e) {
            callback.logError("   - Error: " + e.getMessage());
        }
    }

    private void generateMerge(FreemarkerHandler templateHandler, MergeData mergeData,
                               CodeGenCallback callback) throws IOException, TemplateError {
        String file = mergeData.getFile();
        callback.logInfo(String.format("   - Creating merge %s...", file));
        List<MergeObject> objects = mergeData.getObjects();
        objects.sort(Comparator.comparing(MergeObject::getOrder));
        Map<String, String> data = new HashMap<>();
        data.put("content", mergeContent(templateHandler, objects));
        if (createFile(file, templateHandler, mergeData.getTemplate(), data)) {
            callback.logInfo(String.format("   - %s successfully created!", file));
        } else {
            callback.logInfo(String.format("   - %s already exists. Skipped.", file));
        }
    }

    private boolean createFile(String path, FreemarkerHandler templateHandler,
                               String template, DataInput dataInput) throws IOException, TemplateError {
        if (dataInput.getData() != null) {
            return createFile(path, templateHandler, template, dataInput.getData());
        }
        return createFile(path, templateHandler, template, dataInput.getCsvDataObject(dataFolder));
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
        String lineFeed = "\n";
        for (MergeObject mergeObject : mergeObjects) {
            if (content.length() > 0) {
                content.append(lineFeed);
            }
            String templateContent = templateHandler.render(mergeObject.getTemplate(), mergeObject.getData());
            if (templateContent.endsWith(lineFeed)) {
                templateContent = templateContent.substring(0, templateContent.length() - lineFeed.length());
            }
            content.append(templateContent);
        }
        return content.toString();
    }
}
