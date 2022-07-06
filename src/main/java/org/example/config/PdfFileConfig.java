package org.example.config;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.utils.AppUtility;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PdfFileConfig {

    private String filePath;

    private Object[] excludePages;

    @JsonCreator
    public PdfFileConfig(
            @JsonProperty("filePath") String filePath,
            @JsonProperty("excludePages") Object[] excludePages) throws IOException {
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

    public int[] getPagesToExclude() {
        // Work on supporting ints and string in excludePages
        // String will be converted to ints and strings following the format of page.x - page.y
        // will return an array of generated ranges

        if (this.excludePages != null && this.excludePages.length != 0) {
            final Set<Integer> pages = new HashSet<>();

            Arrays.stream(this.excludePages).forEach(p -> {
                if (Integer.class.isInstance(p)) {
                    pages.add((int) p);
                } else if (String.class.isInstance(p)) {
                    // Validate if a hyphen is present, if it isn't, trim and use directly
                    String numString = String.valueOf(p);

                    if (numString.trim().contains("-")) {
                        String[] split = numString.trim().split("-");
                        if (split.length > 2) {
                            // Invalid
                            return;
                        }

                        int bound1 = Integer.parseInt(split[0].trim());
                        int bound2 = Integer.parseInt(split[1].trim());

                        if (bound1 < 0 || bound2 < 0) {
                            // Pages can't be negative
                            return;
                        }

                        // Generate range
                        pages.addAll(IntStream.range(Math.min(bound1, bound2), Math.max(bound1, bound2) + 1)
                                    .boxed()
                                    .collect(Collectors.toSet()));
                    } else {
                        // Direct number
                        pages.add(Integer.parseInt(numString));
                    }
                } else {
                    System.out.println("Invalid page number: " + p);
                }
            });

            return pages.stream().mapToInt(Integer::intValue).toArray();
        }

        return new int[]{};
    }

    public Optional<PDDocument> getDocument() {
        Optional<PDDocument> optionalPDDocument = Optional.empty();
        try {
            PDDocument pdDocument = AppUtility.loadPdfFromPath(this.getFilePath());
            int[] pagesToExclude = getPagesToExclude();
            if (pagesToExclude != null) {
                pdDocument = this.removePages(pdDocument, pagesToExclude);
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
