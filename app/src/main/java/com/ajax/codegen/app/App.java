package com.ajax.codegen.app;

import com.ajax.codegen.lib.json.JsonParser;
import com.ajax.codegen.lib.template.FreemarkerHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
                logInfo(String.format("  Processing %s...", jsonFile.getName()));
                try {
                    DataInput dataInput = JsonParser.parseFile(jsonFile, DataInput.class);
                    generateFiles(templateHandler, dataInput);
                } catch (IOException e) {
                    logError(String.format("  Error reading %s: %s", jsonFile.getName(), e.getMessage()));
                }
            }
        } else {
            logWarning(String.format("Nothing to process at %s!", dataFolder.getName()));
        }
    }

    private void generateFiles(FreemarkerHandler templateHandler, DataInput dataInput) {
        // todo
    }

    private void logInfo(String message) {
        log.log(Level.INFO, message);
    }

    private void logWarning(String message) {
        log.log(Level.WARNING, message);
    }

    private void logError(String message) {
        log.log(Level.SEVERE, message);
    }
}
