package com.ajax.codegen.app;

import com.ajax.codegen.app.model.DataInput;
import com.ajax.codegen.app.model.TemplateObject;
import com.ajax.codegen.lib.error.TemplateError;
import com.ajax.codegen.lib.json.JsonParser;
import com.ajax.codegen.lib.template.FreemarkerHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
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
        } catch( CmdLineException e ) {
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
        File[] jsonFiles = dataFolder
            .listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".json"));
        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                logInfo(String.format(" - Processing %s...", jsonFile.getName()));
                try {
                    DataInput dataInput = JsonParser.parseFile(jsonFile, DataInput.class);
                    int filesCount = generateFiles(templateHandler, dataInput);
                    logInfo(String.format(" - %d files created for %s.", filesCount, jsonFile.getName()));
                } catch (IOException e) {
                    logError(String.format(" - Error reading %s: %s", jsonFile.getName(), e.getMessage()));
                }
            }
        } else {
            logWarning(String.format("Nothing to process at %s!", dataFolder.getName()));
        }
    }

    private int generateFiles(FreemarkerHandler templateHandler, DataInput dataInput) {
        if (dataInput.getData() == null) {
            return 0;
        }
        AtomicInteger count = new AtomicInteger(0);
        dataInput.getTemplates().forEach((templateObject) -> {
            if (generateFile(templateHandler, templateObject, dataInput.getData())) {
                count.addAndGet(1);
            }
        });
        return count.get();
    }

    private boolean generateFile(FreemarkerHandler templateHandler, TemplateObject templateObject, Object data) {
        File file = new File(outFolder, templateObject.getFile());
        logInfo(String.format("   - Creating %s...", file.getName()));
        if (file.exists()) {
            if (!overwrite) {
                logInfo(String.format("   - %s already exists. Skipped.", file.getName()));
                return false;
            }
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            String content = templateHandler.render(templateObject.getTemplate(), data);
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            logInfo(String.format("   - %s successfully created!", file.getName()));
            return true;
        } catch (IOException | TemplateError e) {
            logError("   - Error: " + e.getMessage());
        }
        return false;
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
