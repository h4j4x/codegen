package io.github.h4j4x.codegen.lib.internal.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
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

    public static String readString(File file) throws IOException {
        if (file != null && file.canRead()) {
            return Files.readString(file.toPath());
        }
        return null;
    }

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
