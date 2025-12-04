package scs_project.cachememorysimulator.model;

public class Cache {
    private int cacheSize;
    private int blockSize;
    private int associativity;
    private int numberOfSets;
    private CacheSet[] sets;
    private ReplacementPolicy policy;

    public Cache(int cacheSize, int blockSize, int associativity, AddressMappingStrategy map, ReplacementPolicy policy) {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.associativity = associativity;
        this.policy = policy;

        if (map instanceof SetAssociativeMappingStrategy) {
            this.numberOfSets = cacheSize / (blockSize * associativity);
            sets = new CacheSet[numberOfSets];
            for (int i = 0; i < numberOfSets; i++) {
                sets[i] = new CacheSet(associativity, blockSize);
            }
        }
        else if (map instanceof DirectMappingStrategy) {
            int numberOfLines = cacheSize / blockSize;
            sets = new CacheSet[numberOfLines];
            for (int i = 0; i < numberOfLines; i++) {
                sets[i] = new CacheSet(1, blockSize);
            }
        }
        else {
            // Fully associative: single set with all lines
            sets = new CacheSet[1];
            sets[0] = new CacheSet(cacheSize / blockSize, blockSize);
        }
    }

    public CacheSet[] getSets() {
        return this.sets;
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    public int getNumberOfSets() {
        return this.numberOfSets;
    }

    public int getAssociativity() {
        return this.associativity;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public ReplacementPolicy getReplacementPolicy() {
        return this.policy;
    }
}