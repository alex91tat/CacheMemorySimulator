package scs_project.cachememorysimulator.model;

public class DirectMappingStrategy implements AddressMappingStrategy {
    private final int offsetBits;
    private final int indexBits;
    private final int tagBits;

    public DirectMappingStrategy(int blockSize, int numberOfSets) {
        this.offsetBits = (int) (Math.log(blockSize) / Math.log(2));
        this.indexBits = (int) (Math.log(numberOfSets) / Math.log(2));
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