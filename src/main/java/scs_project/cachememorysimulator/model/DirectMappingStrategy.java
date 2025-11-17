package scs_project.cachememorysimulator.model;

public class DirectMappingStrategy implements AddressMappingStrategy {
    private int offsetBits; // 'n' bits in Fig 3.4
    private int indexBits;  // 's' bits (Index Field) in Fig 3.4
    private int indexMask;

    @Override
    public void configure(int cacheSize, int associativity, int blockSize) {
        // In Direct Mapping, associativity is ALWAYS 1.
        // Number of lines = Cache Size / Block Size.
        int numLines = cacheSize / blockSize;

        // 1. Calculate Offset Bits (n bits)
        this.offsetBits = (int) (Math.log(blockSize) / Math.log(2));

        // 2. Calculate Index Bits (s bits)
        // In Direct Mapping, the "Set Index" identifies the specific Line.
        this.indexBits = (int) (Math.log(numLines) / Math.log(2));

        // Create a bitmask to extract the index (e.g., if index is 3 bits, mask is 111 -> 7)
        this.indexMask = (1 << indexBits) - 1;
    }

    @Override
    public int getSetIndex(int address) {
        // Formula: (Memory Block Number) mod (Number of Cache Lines)
        // Shifting right by offsetBits gives us the "Block Number".
        // ANDing with indexMask performs the "mod" operation for power-of-2 sizes.
        return (address >> offsetBits) & indexMask;
    }

    @Override
    public int getTag(int address) {
        // Tag is the remaining upper bits after removing Offset and Index
        return address >> (offsetBits + indexBits);
    }

    @Override
    public int reconstructAddress(int tag, int setIndex) {
        return (tag << (offsetBits + indexBits)) | (setIndex << offsetBits);
    }
}