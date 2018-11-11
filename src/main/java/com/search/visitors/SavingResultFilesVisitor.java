package com.search.visitors;

import com.search.Document;
import com.search.exclusionstrategies.ExclusionStrategy;
import com.search.visitors.SavingResultVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class SavingResultFilesVisitor extends SavingResultVisitor {
    private Set<Document> documents =
            Collections.synchronizedSet(new TreeSet<>(Comparator.comparing(Document::getFilePath)));


    private List<ExclusionStrategy> strategies;

    public SavingResultFilesVisitor(List<ExclusionStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public FileVisitResult visitFile(Path directory, BasicFileAttributes attrs) {
        Set<Document> tmpDocs;
        try {
            tmpDocs = getDocuments(directory);
        } catch (IOException e) {
            e.printStackTrace();
            return FileVisitResult.TERMINATE;
        }
        documents.addAll(tmpDocs);
        return FileVisitResult.CONTINUE;
    }

    private Set<Document> getDocuments(Path directory) throws IOException {
        return Files.walk(directory)
                .filter(Files::isRegularFile)
                .map(path -> {
                    File file = path.toFile();
                    return new Document(file.getAbsolutePath(), file.lastModified(), file.length());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
        for (ExclusionStrategy strategy : strategies) {
            if (strategy.exclude(path.toString())) {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    public Set<Document> getDocuments() {
        return documents;
    }
}