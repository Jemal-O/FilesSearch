package exclusionstrategies;

public interface ExclusionStrategy {

    void fillExclusions(String exclusions);

    boolean exclude(String fileName);
}
