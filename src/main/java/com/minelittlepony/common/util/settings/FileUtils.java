package com.minelittlepony.common.util.settings;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

public interface FileUtils {
    char FILE_PATH_EXTENSION_SEPARATOR = '.';
    String FILE_PATH_EXTENSION_REG = "\\.";

    static String getFileExtension(Path path) {
        String[] pair = path.getFileName().toString().split(FILE_PATH_EXTENSION_REG);
        return pair.length > 1 ? pair[1].trim() : "";
    }

    static Stream<Path> getAlternatives(Path path, String[] supportedExtensions) {
        return Arrays.stream(supportedExtensions).map(ext -> changeExtension(path, ext));
    }

    static Path changeExtension(Path path, String newExtension) {
        String extension = getFileExtension(path);
        String sPath = path.toString();
        if (!extension.isBlank()) {
            sPath = sPath.substring(0, sPath.length() - extension.length() - 1);
        }
        return Path.of(sPath + FILE_PATH_EXTENSION_SEPARATOR + newExtension);
    }
}
