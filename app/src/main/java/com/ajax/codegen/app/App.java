package com.ajax.codegen.app;

import java.io.File;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class App {
    @Option(name="-o", usage="output to this folder", required = true)
    private File out;

    public static void main(String[] args) {
        new App().doMain(args);
    }

    private void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            System.out.println("Will out to " + out.getAbsolutePath());
        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
    }
}
