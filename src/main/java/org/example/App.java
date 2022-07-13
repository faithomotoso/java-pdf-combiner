package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.MergerConfig;
import org.example.core.MergerCore;
import org.example.core.MergerCoreImpl;
import org.example.utils.AppUtility;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App {

    private final MergerCore mergerCore = new MergerCoreImpl();

    public static void main(String[] args) {

        App app = new App();

        if (args.length >= 2) {
            app.mergeWithArgs(args);
        } else if (args.length == 1 && AppUtility.isFile(args[0], "json")) {
            app.mergeWithJson(args[0]);
        } else {
            System.out.println("Invalid usage");
            System.out.println("Use with either [path to json config]");
            System.out.println("Or [list of pdf] [final pdf filename]");
            System.out.println("Your args -> " + Arrays.toString(args));
            System.exit(0);
        }

    }

    private void mergeWithArgs(String... args) {

        String newName = args[args.length - 1];

        mergerCore.merge(Arrays.copyOfRange(args, 0, args.length - 1), newName);
    }

    private void mergeWithJson(String jsonPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            MergerConfig config = objectMapper.readValue(new File(jsonPath), MergerConfig.class);

            System.out.println("Config output name: " + config.getOutputFile());

            mergerCore.merge(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
