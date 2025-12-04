package scs_project.cachememorysimulator.model;

public interface AddressMappingStrategy {
    int extractOffset(int address);
    int extractIndex(int address);
    int extractTag(int address);
    int reconstructAddress(int tag, int index, int offset);
}