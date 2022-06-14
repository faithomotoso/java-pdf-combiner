package org.example.core;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

/**
 * Contains methods achievable
 */
public interface MergerCore {

    PDDocument loadPdfFromPath(String path) throws IOException;

    void merge(String[] paths, String newFileName);
}
