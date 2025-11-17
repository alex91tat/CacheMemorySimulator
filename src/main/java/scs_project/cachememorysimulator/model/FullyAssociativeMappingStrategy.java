package scs_project.cachememorysimulator.model;

public class FullyAssociativeMappingStrategy implements AddressMappingStrategy {
    private int offsetBits; // 'n' bits

    @Override
    public void configure(int cacheSize, int associativity, int blockSize) {
        // Figure 3.5 shows ONLY Tag and Block Offset.
        // There is effectively only 1 Set (Set 0), which contains ALL lines.
        this.offsetBits = (int) (Math.log(blockSize) / Math.log(2));
    }

    @Override
    public int getSetIndex(int address) {
        // There is no index field.
        // We always return 0 because there is only one "group" (the whole cache).
        return 0;
    }

    @Override
    public int getTag(int address) {
        // Everything above the offset is the Tag.
        return address >> offsetBits;
    }

    @Override
    public int reconstructAddress(int tag, int setIndex) {
        // setIndex is ignored (it's always 0)
        return tag << offsetBits;
    }
}