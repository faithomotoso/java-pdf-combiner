package org.example.utils;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class AppUtility {

    public static PDDocument loadPdfFromPath(String path) throws IOException {
        return PDDocument.load(new File(path));
    }

    public static boolean isFile(String path, String extension) {
        return path.endsWith(extension);
    }

}
