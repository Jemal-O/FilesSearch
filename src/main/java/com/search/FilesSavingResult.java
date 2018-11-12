package com.search;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class FilesSavingResult {
    public static final String RESULT_FILE = "tmp/result.txt";

    public void saveResult(Set<Document> documents) {
        Path logFile = Paths.get(RESULT_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8)) {
            for (Document document : documents) {
                writer.write(document.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
