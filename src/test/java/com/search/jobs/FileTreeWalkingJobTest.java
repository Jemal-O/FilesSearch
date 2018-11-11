package com.search.jobs;

import com.search.visitors.SavingResultVisitor;
import com.search.visitors.creators.SavingResultFilesVisitorCreator;
import com.search.visitors.creators.VisitorCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileTreeWalkingJobTest {

    private static final String PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\search\\visitors";
    private static final String EXCLUSIONS_DIRECTORY = System.getProperty("user.dir") +
            "\\src\\main\\java\\com\\search\\visitors\\creators";
    private static final String EXCLUSIONS = "-" + EXCLUSIONS_DIRECTORY;
    private static final ExecutorService FILE_SEARCHING_SERVICE = Executors.newCachedThreadPool();

    private SavingResultVisitor visitor;
    private VisitorCreator creator;

    @Before
    public void setUp() throws Exception {
        creator = new SavingResultFilesVisitorCreator();
    }

    @Test
    public void return_AllDocuments() throws Exception {
        String args[] = new String[]{PATH};
        runJob(args);
        Assert.assertEquals(getWholeResult(), visitor.getResultDocuments().toString());
    }

    @Test
    public void return_AllDocumentsWithoutThoseWhichInExclusionDirectory() throws Exception {
        String args[] = new String[]{PATH, EXCLUSIONS};
        runJob(args);
        Assert.assertEquals(getResultWithoutExclusions(), visitor.getResultDocuments().toString());
    }

    private void runJob(String[] args) throws InterruptedException, java.util.concurrent.ExecutionException {
        visitor = creator.getVisitor(args);
        FileTreeWalkingJob job = new FileTreeWalkingJob(Paths.get(args[0]), visitor);
        Future future = FILE_SEARCHING_SERVICE.submit(job);
        future.get();
    }

    private String getResultWithoutExclusions() {
        return "[[\n" +
                "file=D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\SavingResultFilesVisitor.java\n" +
                "date=2018-11-11\n" +
                "size=1440], [\n" +
                "file=D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\SavingResultVisitor.java\n" +
                "date=2018-11-11\n" +
                "size=296]]";
    }

    private String getWholeResult() {
        return "[[\n" +
                "file=D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\SavingResultFilesVisitor.java\n" +
                "date=2018-11-11\n" +
                "size=1440], [\n" +
                "file=D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\SavingResultVisitor.java\n" +
                "date=2018-11-11\n" +
                "size=296], [\n" +
                "file=D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\creators\\SavingResultFilesVisitorCreator.java\n" +
                "date=2018-11-11\n" +
                "size=1508], [\n" +
                "file=D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\creators\\VisitorCreator.java\n" +
                "date=2018-11-11\n" +
                "size=185]]";
    }
}