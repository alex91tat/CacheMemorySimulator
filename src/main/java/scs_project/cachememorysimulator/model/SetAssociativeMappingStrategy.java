package scs_project.cachememorysimulator.model;

public class SetAssociativeMappingStrategy implements AddressMappingStrategy {
    private int offsetBits;
    private int indexBits; // Corresponds to the "Set Number" bits in Figure 3.6
    private int numSets;   // The "Number of Sets" required for the modulo formula

    @Override
    public void configure(int cacheSize, int associativity, int blockSize) {
        // 1. Calculate Number of Sets
        // Formula derived from logic: Total Capacity / (Size of one Set)
        // Size of one set = Associativity * BlockSize
        this.numSets = cacheSize / (associativity * blockSize);

        // 2. Calculate Bits for Offset (n bits)
        this.offsetBits = (int) (Math.log(blockSize) / Math.log(2));

        // 3. Calculate Bits for Set Index (s bits)
        // This is needed to know how much to shift to get the tag
        this.indexBits = (int) (Math.log(numSets) / Math.log(2));
    }

    @Override
    public int getSetIndex(int address) {
        // Step 1: Remove the Block Offset to get the "Block Number"
        int blockNumber = address >> offsetBits;

        // Step 2: Apply the Formula from Page 8
        // "Set Number = (Main Memory Block Number) mod (Number of Sets)"
        return blockNumber % numSets;
    }

    @Override
    public int getTag(int address) {
        // The Tag is everything remaining after the Offset and Set Index bits
        // Structure: | Tag | Set Number | Block Offset | [cite: 211, 212]
        return address >> (offsetBits + indexBits);
    }

    @Override
    public int reconstructAddress(int tag, int setIndex) {
        // To reverse the process:
        // Shift Tag up past the Set and Offset bits
        // Shift Set Index up past the Offset bits
        return (tag << (offsetBits + indexBits)) | (setIndex << offsetBits);
    }
}