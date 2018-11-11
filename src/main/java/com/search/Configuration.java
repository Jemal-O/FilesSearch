package com.search;

import com.search.jobs.FileTreeWalkingJob;
import com.search.visitors.SavingResultVisitor;
import com.search.visitors.creators.SavingResultFilesVisitorCreator;
import com.search.visitors.creators.VisitorCreator;

import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.*;

public class Configuration {

    private static final ExecutorService FILE_SEARCHING_SERVICE = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService DOT_DRAW_SERVICE = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService PIPE_DRAW_SERVICE = Executors.newScheduledThreadPool(1);

    public static void main(String... args) {
        VisitorCreator creator = new SavingResultFilesVisitorCreator();
        SavingResultVisitor visitor = creator.getVisitor(args);
        runTasks(args, visitor);
        saveFoundFilesTask(visitor.getResultDocuments());
    }

    private static void runTasks(String[] myArgs, SavingResultVisitor visitor) {
        runEvery6SecondsDotsTask();
        runEveryMinutePipeTask();
        tryFilesSearchingTask(myArgs, visitor);
        exitServices();
    }

    private static void runEvery6SecondsDotsTask() {
        Runnable drawRunnable = () -> System.out.println(".");
        DOT_DRAW_SERVICE.scheduleAtFixedRate(drawRunnable, 0, 6, TimeUnit.SECONDS);
    }

    private static void runEveryMinutePipeTask() {
        Runnable drawRunnable = () -> System.out.println("|");
        PIPE_DRAW_SERVICE.scheduleAtFixedRate(drawRunnable, 0, 1, TimeUnit.MINUTES);
    }

    private static void tryFilesSearchingTask(String[] myArgs, SavingResultVisitor visitor) {
        try {
            filesSearchingTask(myArgs, visitor);
        } catch (ExecutionException | InterruptedException e) {
            exitServices();
            e.printStackTrace();
        }
    }

    private static void filesSearchingTask(String[] args, SavingResultVisitor visitor)
            throws ExecutionException, InterruptedException {
        FileTreeWalkingJob job = new FileTreeWalkingJob(Paths.get(args[0]), visitor);
        Future future = FILE_SEARCHING_SERVICE.submit(job);
        future.get();
    }

    private static void exitServices() {
        FILE_SEARCHING_SERVICE.shutdown();
        DOT_DRAW_SERVICE.shutdown();
        PIPE_DRAW_SERVICE.shutdown();
    }

    private static void saveFoundFilesTask(Set<Document> documents) {
        FilesSavingResult result = new FilesSavingResult();
        result.saveResult(documents);
    }
}
