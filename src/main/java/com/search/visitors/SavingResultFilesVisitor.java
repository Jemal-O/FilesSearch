package com.search.visitors;

import com.search.Document;
import com.search.exclusionstrategies.ExclusionStrategy;
import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SavingResultFilesVisitor extends SavingResultVisitor {

    //Выбран TreeSet потому что в данном случае у нас должна быть отсортировання коллекция,
    //TreeSet один из наиболее удобных вариантов.
    // При этом, у нас не мб два полностью идентичных пути, поэтому TreeSet в данном случае подходит
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