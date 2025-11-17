package scs_project.cachememorysimulator.model;

public class FifoPolicy implements ReplacementPolicy {
    @Override
    public CacheLine findVictim(CacheSet set) {
        // Phase 1: Check for empty line
        CacheLine[] lines = set.getLines();
        for (CacheLine line : lines) {
            if (!line.isValid()) {
                set.moveToHead(line);
                return line;
            }
        }

        CacheLine victim = set.getTail();
        set.moveToHead(victim);
        return victim;
    }

    @Override
    public void onAccess(CacheSet set, CacheLine line) {
        // doesn t matter for FIFO policy
    }
}
