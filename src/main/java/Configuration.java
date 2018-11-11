import exclusionstrategies.DirectoriesExclusionStrategy;
import exclusionstrategies.ExclusionStrategy;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Configuration {

    private static final Map<String, ExclusionStrategy> EXCLUSION_STRATEGIES = new HashMap<String, ExclusionStrategy>() {{
        put("-", new DirectoriesExclusionStrategy());
    }};
    private static final ExecutorService FILE_SEARCHING_SERVICE = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService DOT_DRAW_SERVICE = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService PIPE_DRAW_SERVICE = Executors.newScheduledThreadPool(1);

    public static void main(String... args) {
        RetrievingFilesVisitor visitor = getRetrievingFilesVisitor(args);
        runTasks(args, visitor);
    }

    private static void runTasks(String[] myArgs, RetrievingFilesVisitor visitor) {
        runEvery6SecondsDotsTask();
        runEveryMinutePipeTask();
        tryFilesSearchingTask(myArgs, visitor);
        exitServices();
    }

    private static void tryFilesSearchingTask(String[] myArgs, RetrievingFilesVisitor visitor) {
        try {
            filesSearchingTask(myArgs, visitor);
        } catch (ExecutionException | InterruptedException e) {
            exitServices();
            e.printStackTrace();
        }
    }

    private static void runEvery6SecondsDotsTask() {
        Runnable drawRunnable = () -> System.out.println(".");
        DOT_DRAW_SERVICE.scheduleAtFixedRate(drawRunnable, 0, 6, TimeUnit.SECONDS);
    }

    private static void runEveryMinutePipeTask() {
        Runnable drawRunnable = () -> System.out.println("|");
        PIPE_DRAW_SERVICE.scheduleAtFixedRate(drawRunnable, 0, 1, TimeUnit.MINUTES);
    }

    private static void filesSearchingTask(String[] args, RetrievingFilesVisitor visitor) throws ExecutionException, InterruptedException {
        FileTreeWalkingJob job = new FileTreeWalkingJob(Paths.get(args[0]), visitor);
        Future future = FILE_SEARCHING_SERVICE.submit(job);
        future.get();
    }

    private static RetrievingFilesVisitor getRetrievingFilesVisitor(String... args) {
        List<ExclusionStrategy> exclusionStrategies = defineExclusionStrategies(args);
        return new RetrievingFilesVisitor(exclusionStrategies);
    }

    private static List<ExclusionStrategy> defineExclusionStrategies(String... args) {
        List<ExclusionStrategy> applicableStrategies = new ArrayList<>();
        for (String key : EXCLUSION_STRATEGIES.keySet()) {
            for (int i = 1; i < args.length; i++) {
                if (args[i].startsWith(key)) {
                    ExclusionStrategy strategy = EXCLUSION_STRATEGIES.get(key);
                    strategy.fillExclusions(args[i]);
                    applicableStrategies.add(strategy);
                }
            }
        }
        return applicableStrategies;
    }

    private static void exitServices() {
        FILE_SEARCHING_SERVICE.shutdown();
        DOT_DRAW_SERVICE.shutdown();
        PIPE_DRAW_SERVICE.shutdown();
    }
}
