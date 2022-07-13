package org.example.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.utils.AppUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MergerConfig {
    private String outputFile;

//    @JsonDeserialize(as = ArrayList.class, contentAs = PdfFileConfig.class)
    private List<PdfFileConfig> fileConfigs;

    @JsonCreator
    MergerConfig(@JsonProperty("outputFile") String outputFile, @JsonProperty("fileConfigs") List<PdfFileConfig> fileConfigs) throws IOException {
        this.setOutputFile(outputFile);
        this.fileConfigs = fileConfigs;
    }

    public void setOutputFile(String outputFile) throws IOException {
        if (!AppUtility.isFile(outputFile, "pdf")) {
            throw new IOException("Output file [" + outputFile + "] must end with .pdf");
        }
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public List<PdfFileConfig> getFileConfigs() {
        return this.fileConfigs;
    }
}
