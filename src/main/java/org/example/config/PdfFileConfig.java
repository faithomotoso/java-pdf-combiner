package org.example.config;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.utils.AppUtility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class PdfFileConfig {

    private String filePath;

    private int[] excludePages;

    @JsonCreator
    public PdfFileConfig(
                        @JsonProperty("filePath") String filePath,
                        @JsonProperty("excludePages") int[] excludePages) throws IOException {
        if (!AppUtility.isFile(filePath, "pdf")) {
            throw new IOException("File path [" + filePath + "] should be a pdf");
        }
        this.filePath = filePath;
        this.excludePages = excludePages;
        if (excludePages != null) {
            System.out.println("Exclude pages: " + Arrays.toString(excludePages));
        }
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Optional<PDDocument> getDocument() {
        Optional<PDDocument> optionalPDDocument = Optional.empty();
        try {
            optionalPDDocument = Optional.ofNullable(AppUtility.loadPdfFromPath(getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return optionalPDDocument;
    }
}
