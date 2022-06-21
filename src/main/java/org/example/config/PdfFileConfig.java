package org.example.config;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.utils.AppUtility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PdfFileConfig {

    private String filePath;

    private int[] excludePages;

    @JsonCreator
    public PdfFileConfig(
                        @JsonProperty("filePath") String filePath,
                        @JsonProperty("excludePages") int[] excludePages) throws IOException {
        if (!AppUtility.isFile(filePath, "pdf")) {
            throw new IOException("File [" + filePath + "] should be a pdf");
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
            PDDocument pdDocument = AppUtility.loadPdfFromPath(this.getFilePath());
            if (excludePages != null) {
                pdDocument = this.removePages(pdDocument, this.excludePages);
            }
            System.out.println("Returning for this file: " + this.getFilePath());
            optionalPDDocument = Optional.ofNullable(pdDocument);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return optionalPDDocument;
    }

    private PDDocument removePages(PDDocument document, int[] pages) {
        Arrays.sort(pages);

        // Page numbers from PDDocument are zero based
        Set<Integer> pagesSet = Arrays.stream(pages).boxed()
                                .map(i -> --i)
                                .collect(Collectors.toSet());

        // Create a new doc and extract pages needed in a loop
        try {
            final PDDocument newDocument = new PDDocument();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                if (pagesSet.contains(i)) {
                    // Skip
                    continue;
                }
                newDocument.addPage(document.getPage(i));
            }

            return newDocument;
        } catch (Exception e) {
            System.out.println("Error removing pages: " + e.getLocalizedMessage());
        }

        // If an error occurs, return the page back
        return document;
    }
}
