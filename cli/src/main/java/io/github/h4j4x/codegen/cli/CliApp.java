package io.github.h4j4x.codegen.cli;

import io.github.h4j4x.codegen.lib.CodeGen;
import io.github.h4j4x.codegen.lib.CodeGenCallback;
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

public class CliApp implements CodeGenCallback {
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
    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    private boolean overwrite = false;

    @Option(name="--read-recursive", usage="Deep data folder read mode")
    @SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
    private boolean readRecursive = false;

    public static void main(String[] args) {
        new CliApp(args);
    }

    public CliApp(String[] args) {
        doMain(args);
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
            CodeGen codeGen = new CodeGen(dataFolder, templatesFolder, outFolder, overwrite, readRecursive);
            codeGen.generateCode(this);
        } catch(CmdLineException e) {
            logError(e.getMessage());
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            parser.printUsage(outStream);
            logError(String.format("Usage:\n%s", outStream.toString(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            logError(e.getMessage());
        }
    }

    @Override
    public void logInfo(String message) {
        if (verbose) {
            log.log(Level.INFO, message);
        }
    }

    @Override
    public void logWarning(String message) {
        if (verbose) {
            log.log(Level.WARNING, message);
        }
    }

    @Override
    public void logError(String message) {
        if (verbose) {
            log.log(Level.SEVERE, message);
        }
    }
}
