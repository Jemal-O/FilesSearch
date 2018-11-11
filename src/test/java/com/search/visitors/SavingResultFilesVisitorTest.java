package com.search.visitors;

import com.search.Document;
import com.search.visitors.creators.SavingResultFilesVisitorCreator;
import com.search.visitors.creators.VisitorCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.FileVisitResult;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;

public class SavingResultFilesVisitorTest {

    private static final String PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\search\\visitors";
    private static final String EXCLUSIONS_DIRECTORY = System.getProperty("user.dir") +
            "\\src\\main\\java\\com\\search\\visitors\\creators";
    private static final String EXCLUSIONS = "-" + EXCLUSIONS_DIRECTORY;

    private static final String ARGS[] = new String[]{PATH, EXCLUSIONS};

    SavingResultVisitor visitor;
    VisitorCreator creator;

    @Before
    public void setUp() throws Exception {
        creator = new SavingResultFilesVisitorCreator();
    }

    @Test
    public void return_ContinueAndNoFilesAddedIfItIsDirectory() throws Exception {
        visitor = creator.getVisitor(ARGS);
        FileVisitResult result = visitor.visitFile(Paths.get(PATH), Mockito.mock(BasicFileAttributes.class, Mockito.CALLS_REAL_METHODS));
        Assert.assertEquals(FileVisitResult.CONTINUE, result);
        Assert.assertEquals(new TreeSet<Document>(), visitor.getResultDocuments());
    }

    @Test
    public void return_ContinueAndFileAddedIfItIsRegularFile() throws Exception {
        visitor = creator.getVisitor(ARGS);
        FileVisitResult result = visitor.visitFile(Paths.get(PATH + "\\SavingResultFilesVisitor.java"),
                Mockito.mock(BasicFileAttributes.class, Mockito.CALLS_REAL_METHODS));
        Assert.assertEquals(FileVisitResult.CONTINUE, result);
        Assert.assertEquals(getExpected(), visitor.getResultDocuments().toString());
    }

    private String getExpected() {
        return "[[\n" +
                "filePath='D:\\1_projects\\filesSearch\\src\\main\\java\\com\\search\\visitors\\SavingResultFilesVisitor.java\n" +
                "date=2018-11-11\n" +
                "size=1440]]";
    }

    @Test
    public void return_SkipSubTreeIfFolderInListOfExclusions() throws Exception {
        visitor = creator.getVisitor(ARGS);
        FileVisitResult result = visitor.preVisitDirectory(Paths.get(EXCLUSIONS_DIRECTORY), Mockito.mock(BasicFileAttributes.class, Mockito.CALLS_REAL_METHODS));
        Assert.assertEquals(FileVisitResult.SKIP_SUBTREE, result);
    }


}