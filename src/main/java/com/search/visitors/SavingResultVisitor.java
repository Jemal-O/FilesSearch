package com.search.visitors;

import com.search.Document;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.Set;

public abstract class SavingResultVisitor extends SimpleFileVisitor<Path> {

    public abstract Set<Document> getResultDocuments();
}
