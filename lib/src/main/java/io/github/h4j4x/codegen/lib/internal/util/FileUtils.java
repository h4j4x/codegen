package io.github.h4j4x.codegen.lib.internal.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * File handling utilities.
 */
public class FileUtils {
    /**
     * Get a file.
     * @param parent the parent file. (can be null)
     * @param path the path relative to parent.
     * @return the file.
     */
    public static File getFile(File parent, String path) {
        if (path != null) {
            String[] parts = path.trim()
                .replaceAll("\\\\", "/")
                .split("/");
            File file = parent;
            for (String part : parts) {
                file = new File(file, part);
            }
            return file;
        }
        return null;
    }

    /**
     * Read a file as string.
     * @param file the file to be read.
     * @return the string content.
     * @throws IOException if an I/O error occurs.
     */
    public static String readString(File file) throws IOException {
        if (file != null && file.canRead()) {
            return Files.readString(file.toPath());
        }
        return null;
    }

    /**
     * Get files from folder.
     * @param dir the folder to be read.
     * @param filter the filter to apply.
     * @param recursive if inside folders should be read too.
     * @return the list of files.
     */
    public static List<File> readFiles(File dir, FileFilter filter, boolean recursive) {
        List<File> files = new LinkedList<>();
        if (dir.canRead() && dir.isDirectory()) {
            File[] list = dir.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (filter.accept(file)) {
                        files.add(file);
                    }
                    if (recursive && file.isDirectory()) {
                        files.addAll(readFiles(file, filter, true));
                    }
                }
            }
        }
        return files;
    }
}
