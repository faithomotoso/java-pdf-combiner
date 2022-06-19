package org.example.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.config.MergerConfig;

import java.io.IOException;

/**
 * Contains methods achievable
 */
public interface MergerCore {

    void merge(String[] paths, String newFileName);

    void merge(MergerConfig mergerConfig);
}
