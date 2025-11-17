package scs_project.cachememorysimulator.model;

public class Cache {
    private CacheSet[] sets;
    private AddressMappingStrategy mappingStrategy;
    private ReplacementPolicy replacementPolicy;
    private int blockSize;

    public Cache(int cacheSize, int associativity, int blockSize,
                 AddressMappingStrategy mappingStrategy,
                 ReplacementPolicy replacementPolicy) {

        this.blockSize = blockSize;
        this.mappingStrategy = mappingStrategy;
        this.replacementPolicy = replacementPolicy;

        // Configure the specific mapping strategy (Direct, Set, or Fully)
        this.mappingStrategy.configure(cacheSize, associativity, blockSize);

        // Calculate number of sets based on mapping
        // For Fully Associative, this results in 1 set.
        // For Direct, it results in cacheSize/blockSize sets.
        int numSets = cacheSize / (blockSize * associativity);

        this.sets = new CacheSet[numSets];
        for (int i = 0; i < numSets; i++) {
            this.sets[i] = new CacheSet(associativity, blockSize);
        }
    }

    public CacheLine read(int address) {
        int setIndex = mappingStrategy.getSetIndex(address);
        int tag = mappingStrategy.getTag(address);

        CacheSet set = sets[setIndex];
        CacheLine line = set.findLine(tag);

        if (line != null) {
            // Notify replacement policy (e.g., LRU updates list head)
            replacementPolicy.onAccess(set, line);
            return line;
        }
        return null;
    }

    public CacheLine installBlock(int address, int[] data) {
        int setIndex = mappingStrategy.getSetIndex(address);
        int tag = mappingStrategy.getTag(address);
        CacheSet set = sets[setIndex];

        // 1. Find a victim using the policy (Random, LRU, FIFO) [cite: 215]
        CacheLine victim = replacementPolicy.findVictim(set);

        // 2. If the victim is valid, we capture its state for Write-Back checking
        CacheLine evictedState = null;
        if (victim.isValid()) {
            evictedState = new CacheLine(blockSize);
            evictedState.setTag(victim.getTag());
            evictedState.setData(victim.getData());
            evictedState.setDirty(victim.isDirty());
            evictedState.setValid(true);
        }

        // 3. Update the internal Map and Data
        set.updateTagMap(victim.getTag(), tag, victim);
        victim.loadData(data, tag);

        // For LRU/FIFO, ensure the new line is treated as "new" (e.g., move to head or enqueue)
        replacementPolicy.onAccess(set, victim);

        return evictedState;
    }

    public int getSetIndex(int address) {
        return mappingStrategy.getSetIndex(address);
    }

    public int reconstructAddress(int tag, int setIndex) {
        return mappingStrategy.reconstructAddress(tag, setIndex);
    }
}