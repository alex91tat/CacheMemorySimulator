package scs_project.cachememorysimulator.model;

public class FullyAssociativeMappingStrategy implements AddressMappingStrategy {
    private final int offsetBits;

    public FullyAssociativeMappingStrategy(int blockSize) {
        this.offsetBits = (int) (Math.log(blockSize) / Math.log(2));
    }

    @Override
    public int extractOffset(int address) {
        return address & ((1 << offsetBits) - 1);
    }

    @Override
    public int extractIndex(int address) {
        return 0; // In fully associative cache, there's only one set
    }

    @Override
    public int extractTag(int address) {
        return address >>> offsetBits; // Everything except offset becomes tag
    }

    @Override
    public int reconstructAddress(int tag, int index, int offset) {
        return (tag << offsetBits) | offset;
        // Note: index is ignored in fully associative mapping
    }
}