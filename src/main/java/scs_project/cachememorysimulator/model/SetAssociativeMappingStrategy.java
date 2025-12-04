package scs_project.cachememorysimulator.model;

public class SetAssociativeMappingStrategy implements AddressMappingStrategy {
    private final int offsetBits;
    private final int indexBits;
    private final int tagBits;

    public SetAssociativeMappingStrategy(int blockSize, int numberOfSets, int associativity) {
        this.offsetBits = (int) (Math.log(blockSize) / Math.log(2));
        this.indexBits = (int) (Math.log(numberOfSets/associativity) / Math.log(2));
        this.tagBits = 32 - offsetBits - indexBits;
    }

    @Override
    public int extractOffset(int address) {
        return address & ((1 << offsetBits) - 1);
    }

    @Override
    public int extractIndex(int address) {
        return (address >>> offsetBits) & ((1 << indexBits) - 1);
    }

    @Override
    public int extractTag(int address) {
        return address >>> (offsetBits + indexBits);
    }

    @Override
    public int reconstructAddress(int tag, int index, int offset) {
        return (tag << (indexBits + offsetBits)) |
                (index << offsetBits) |
                offset;
    }
}