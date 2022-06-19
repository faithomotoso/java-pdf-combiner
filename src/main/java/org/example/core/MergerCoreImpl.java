package org.example.core;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.config.MergerConfig;
import org.example.config.PdfFileConfig;
import org.example.utils.AppUtility;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MergerCoreImpl implements MergerCore {

    /**
     * This handles merging with args passed in the command line
     *
     * @param paths       - paths to pdfs to be merged
     * @param newFileName - file name of the final pdf
     */
    @Override
    public void merge(String[] paths, String newFileName) {

        if (paths.length == 1) {
            // Only 1 file, nothing to merge
            return;
        }

        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        try (PDDocument newDoc = AppUtility.loadPdfFromPath(paths[0])) {
            String[] rem = Arrays.copyOfRange(paths, 1, paths.length);

            Arrays.stream(rem).forEach(p -> {
                try (PDDocument doc = AppUtility.loadPdfFromPath(p)) {
                    System.out.println("Merging: " + p);
                    mergerUtility.appendDocument(newDoc, doc);
                } catch (Exception e) {
                    System.out.println("Unable to merge file " + p);
                    e.printStackTrace();
                }
            });

            // Save final doc
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            newDoc.save(newFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void merge(MergerConfig mergerConfig) {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

        // Starting with the first document
        List<PDDocument> documents = mergerConfig.getFileConfigs().stream()
                .map(PdfFileConfig::getDocument)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableList());

        if (documents.isEmpty() || documents.size() == 1) {
            return;
        }

        try (PDDocument mergeDoc = documents.get(0)) {
            documents.stream().skip(1).forEach(d -> {
                try {
                    pdfMergerUtility.appendDocument(mergeDoc, d);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            mergeDoc.save(mergerConfig.getOutputFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
