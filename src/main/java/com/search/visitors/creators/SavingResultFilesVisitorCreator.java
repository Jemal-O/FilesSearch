package com.search.visitors.creators;

import com.search.exclusionstrategies.DirectoriesExclusionStrategy;
import com.search.exclusionstrategies.ExclusionStrategy;
import com.search.visitors.SavingResultFilesVisitor;
import com.search.visitors.SavingResultVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavingResultFilesVisitorCreator implements VisitorCreator {

    private static final Map<String, ExclusionStrategy> EXCLUSION_STRATEGIES = new HashMap<String, ExclusionStrategy>() {{
        put("-", new DirectoriesExclusionStrategy());
    }};

    @Override
    public SavingResultVisitor getVisitor(String... args) {
        List<ExclusionStrategy> exclusionStrategies = defineExclusionStrategies(args);
        return new SavingResultFilesVisitor(exclusionStrategies);
    }

    private List<ExclusionStrategy> defineExclusionStrategies(String... args) {
        List<ExclusionStrategy> applicableStrategies = new ArrayList<>();
        for (String key : EXCLUSION_STRATEGIES.keySet()) {
            for (int i = 1; i < args.length; i++) {
                if (args[i] != null && args[i].startsWith(key)) {
                    ExclusionStrategy strategy = EXCLUSION_STRATEGIES.get(key);
                    strategy.fillExclusions(args[i]);
                    applicableStrategies.add(strategy);
                }
            }
        }
        return applicableStrategies;
    }

}
