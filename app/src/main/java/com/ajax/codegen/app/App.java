package com.ajax.codegen.app;

import com.ajax.codegen.app.model.DataInput;
import com.ajax.codegen.lib.error.TemplateError;
import com.ajax.codegen.lib.json.JsonParser;
import com.ajax.codegen.lib.template.FreemarkerHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.Messages;

public class App {
    private static final Logger log = Logger.getLogger(App.class.getName());

    @Option(name="-d", usage="Data folder", required = true)
    private File dataFolder;

    @Option(name="-t", usage="Templates folder", required = true)
    private File templatesFolder;

    @Option(name="-o", usage="Output folder", required = true)
    private File outFolder;

    @Option(name="--verbose", usage="Verbose mode")
    @SuppressWarnings("FieldMayBeFinal")
    private boolean verbose = false;

    public static void main(String[] args) {
        new App().doMain(args);
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
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void generateFiles() throws IOException {
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
                    generateFiles(templateHandler, dataInput);
                } catch (IOException e) {
                    logError(String.format(" - Error reading %s: %s", jsonFile.getName(), e.getMessage()));
                }
            }
        } else {
            logWarning(String.format("Nothing to process at %s!", dataFolder.getName()));
        }
    }

    private void generateFiles(FreemarkerHandler templateHandler, DataInput dataInput) {
        dataInput.getTemplatesFilesMap().forEach((template, fileName) -> {
            File file = new File(outFolder, fileName);
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                String content = templateHandler.render(template, dataInput.getData());
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
                logInfo(String.format(" -- %s successfully created!", file.getName()));
            } catch (IOException | TemplateError e) {
                logError(" -- Error: " + e.getMessage());
            }
        });
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
