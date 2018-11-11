import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTreeWalkingJob implements Runnable {
    private Path path;
    private RetrievingFilesVisitor visitor;


    public FileTreeWalkingJob(Path path, RetrievingFilesVisitor visitor) {
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
