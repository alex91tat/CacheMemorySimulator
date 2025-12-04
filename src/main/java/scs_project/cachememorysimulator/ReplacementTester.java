package scs_project.cachememorysimulator;


import scs_project.cachememorysimulator.model.*;

public class ReplacementTester {

    private final CacheSet set;
    private final ReplacementPolicy policy;

    public ReplacementTester(int associativity, int blockSize, ReplacementPolicy policy) {
        this.set = new CacheSet(associativity, blockSize);
        this.policy = policy;
    }

    public void insertTag(int tag) {
        CacheLine found = set.findLine(tag);
        if (found != null && found.isValid()) {
            System.out.println("Hit: tag " + tag);
            policy.onAccess(set, found);
            return;
        }

        CacheLine victim = policy.findVictim(set);

        boolean evicted = victim.isValid();
        int oldTag = victim.getTag();

        if (evicted)
            set.updateTagMap(oldTag, tag, victim);
        else
            set.updateTagMap(-1, tag, victim);

        victim.setValid(true);
        victim.setTag(tag);
        victim.setDirty(false);

        policy.onAccess(set, victim);

        if (evicted)
            System.out.println("MISS replacing tag " + oldTag + " → inserted " + tag);
        else
            System.out.println("MISS inserting into empty line → " + tag);
    }

    public void printState() {
        System.out.println("\nCurrent Set State:");
        int i = 0;
        for (CacheLine line : set.getLines()) {
            String s = line.isValid() ? ("Tag=" + line.getTag()) : "EMPTY";
            System.out.println("  Line " + i + ": " + s);
            i++;
        }
        System.out.println();
    }
}

