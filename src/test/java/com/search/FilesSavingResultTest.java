package com.search;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static com.search.FilesSavingResult.RESULT_FILE;

public class FilesSavingResultTest {
    FilesSavingResult result;

    @Before
    public void setUp() throws Exception {
        result = new FilesSavingResult();
    }

    @Test
    public void saveResult() throws Exception {
        result.saveResult(prepareDocuments());
        Assert.assertEquals(prepareDocuments().toString(), "[" + readResult() + "]");
    }

    private Set<Document> prepareDocuments() {
        Document doc = new Document("\\com\\search\\visitors\\SavingResultFilesVisitor.java", 1255665448, 1448);
        Set<Document> docs = new HashSet<>();
        docs.add(doc);
        return docs;
    }

    private String readResult() {
        StringBuilder builder = new StringBuilder();
        tryToReadResultFile(builder);
        return builder.toString();
    }

    private void tryToReadResultFile(StringBuilder builder) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(RESULT_FILE), StandardCharsets.UTF_8)) {
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}