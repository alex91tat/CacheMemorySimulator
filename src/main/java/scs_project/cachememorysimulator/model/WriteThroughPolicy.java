package scs_project.cachememorysimulator.model;

public class WriteThroughPolicy implements WritingPolicy {
    @Override
    public void handleWrite(MainMemory mainMemory, CacheLine line, int address, String value) {
        // we update both cache and main memory immediately
        line.setData(value);
        mainMemory.write(address, value);
        line.setDirty(false); // In write-through, lines are n
    }

    @Override
    public void handleEviction(MainMemory mainMemory, CacheLine line, int address) {
        // data is already in the main memory
    }

}
