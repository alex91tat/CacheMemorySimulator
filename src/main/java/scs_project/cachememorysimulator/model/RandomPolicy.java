package scs_project.cachememorysimulator.model;

import java.util.Random;

public class RandomPolicy implements ReplacementPolicy {
    private Random random = new Random();

    @Override
    public CacheLine findVictim(CacheSet set) {
        CacheLine[] lines = set.getLines();

        for (CacheLine line : lines) {
            if (!line.isValid()) {
                return line;
            }
        }

        return lines[random.nextInt(lines.length)];
    }

    @Override
    public void onAccess(CacheSet set, CacheLine line) {
        // doesn t matter for random policy
    }
}
