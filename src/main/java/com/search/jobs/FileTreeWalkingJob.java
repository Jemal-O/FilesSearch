package com.search.jobs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

public class FileTreeWalkingJob implements Runnable {
    private Path path;
    private SimpleFileVisitor<Path> visitor;


    public FileTreeWalkingJob(Path path, SimpleFileVisitor<Path> visitor) {
        this.path = path;
        this.visitor = visitor;
    }

    @Override
    public void run() {
        try {
            Files.walkFileTree(path, visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
