package scs_project.cachememorysimulator.model;

public interface AddressMappingStrategy {
    void configure(int cacheSize, int associativity, int blockSize);
    int getSetIndex(int address);
    int getTag(int address);
    int reconstructAddress(int tag, int setIndex);
}