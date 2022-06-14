package org.example;

import org.example.core.MergerCore;
import org.example.core.MergerCoreImpl;
import org.example.utils.PdfUtility;
import java.util.Arrays;

public class App {

    private static final PdfUtility pdfUtility = new PdfUtility();

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: [list of pdfs] [combined pdf name]");
            System.exit(0);
        }

        MergerCore mergerCore = new MergerCoreImpl();

        String newName = args[args.length - 1];

        mergerCore.merge(Arrays.copyOfRange(args, 0, args.length - 1), newName);
    }
}
