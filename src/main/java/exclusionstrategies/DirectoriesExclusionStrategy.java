package exclusionstrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoriesExclusionStrategy implements ExclusionStrategy {

    private static final Pattern PATTERN = Pattern.compile("((\\\\\\\\)|([A-Z]:))([a-z]|[A-Z]|\\\\|[0-9]|\\s|\\.|)+");
    private final List<String> exclusions = new ArrayList<>();

    @Override
    public void fillExclusions(String exclusions) {
        Matcher matcher = PATTERN.matcher(exclusions);
        while (matcher.find()) {
            this.exclusions.add(matcher.group());
        }
    }

    @Override
    public boolean exclude(String file) {
        return exclusions.contains(file);
    }
}
