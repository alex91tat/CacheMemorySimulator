package scs_project.cachememorysimulator.model;

public class Statistics {
    private int hits = 0;
    private int misses = 0;
    private int totalAccesses = 0;

    public void recordHit() {
        hits++;
        totalAccesses++;
    }

    public void recordMiss() {
        misses++;
        totalAccesses++;
    }

    public int getTotalAccesses() {
        return totalAccesses;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public double getHitRate() {
        return totalAccesses == 0 ? 0 : (double) hits / totalAccesses;
    }

    public double getMissRate() {
        return totalAccesses == 0 ? 0 : (double) misses / totalAccesses;
    }

    @Override
    public String toString() {
        return "Statistics: \n" +
                "Hits=" + hits +
                ", Misses=" + misses +
                ", Hit Rate=" + String.format("%.2f%%", getHitRate() * 100) +
                ", Miss Rate=" + String.format("%.2f%%", getMissRate() * 100);
    }
}