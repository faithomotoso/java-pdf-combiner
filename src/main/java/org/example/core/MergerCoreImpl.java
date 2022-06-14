package org.example.core;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MergerCoreImpl implements MergerCore{
    @Override
    public PDDocument loadPdfFromPath(String path) throws IOException {
        return PDDocument.load(new File(path));
    }

    /**
     * This handles merging with args passed in the command line
     * @param paths - paths to pdfs to be merged
     * @param newFileName - file name of the final pdf
     */
    @Override
    public void merge(String[] paths, String newFileName) {

        if (paths.length == 1) {
            // Only 1 file, nothing to merge
            return;
        }

        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        try (PDDocument newDoc = loadPdfFromPath(paths[0])) {
            String[] rem = Arrays.copyOfRange(paths, 1, paths.length);

            Arrays.stream(rem).forEach(p -> {
                try (PDDocument doc = loadPdfFromPath(p)) {
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

}
