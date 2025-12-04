package scs_project.cachememorysimulator.model;


public interface WritingPolicy {
    void handleWrite(MainMemory mainMemory, CacheLine line, int address, String value);
    void handleEviction(MainMemory mainMemory, CacheLine line, int address);
}
