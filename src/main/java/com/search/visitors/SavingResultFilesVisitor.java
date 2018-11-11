package com.search.visitors;

import com.search.Document;
import com.search.exclusionstrategies.ExclusionStrategy;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SavingResultFilesVisitor extends SavingResultVisitor {
    private Set<Document> documents =
            Collections.synchronizedSet(new TreeSet<>(Comparator.comparing(Document::getFilePath)));


    private List<ExclusionStrategy> strategies;

    public SavingResultFilesVisitor(List<ExclusionStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public FileVisitResult visitFile(Path directory, BasicFileAttributes attrs) {
        File file = directory.toFile();
        if (!file.isDirectory()) {
            documents.add(new Document(file.getAbsolutePath(), file.lastModified(), file.length()));
        }
        return FileVisitResult.CONTINUE;
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

    public Set<Document> getResultDocuments() {
        return documents;
    }
}