package scs_project.cachememorysimulator.model;

public class Statistics {
    private int hits = 0;
    private int misses = 0;
    private int totalAccesses = 0;
    private int dirtyEvictions = 0;

    public void recordHit() {
        hits++;
        totalAccesses++;
    }

    public void recordMiss() {
        misses++;
        totalAccesses++;
    }

    public void recordDirtyEviction() {
        dirtyEvictions++;
    }

    // Hit Ratio = Total Hits / Total Accesses [cite: 131]
    public double getHitRate() {
        return totalAccesses == 0 ? 0 : (double) hits / totalAccesses;
    }

    // Miss Ratio = Total Misses / Total Accesses [cite: 134]
    public double getMissRate() {
        return totalAccesses == 0 ? 0 : (double) misses / totalAccesses;
    }

    @Override
    public String toString() {
        return "Statistics: \n" +
                "Hits=" + hits +
                ", Misses=" + misses +
                ", Hit Rate=" + String.format("%.2f%%", getHitRate() * 100) +
                ", Dirty Evictions=" + dirtyEvictions +
                '}';
    }
}