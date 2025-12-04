package scs_project.cachememorysimulator.model;

public class WriteBackPolicy implements WritingPolicy {
    @Override
    public void handleWrite(MainMemory mainMemory, CacheLine line, int address, String value) {
        // Write only to cache
        line.setData(value);
        line.setDirty(true);
    }


    @Override
    public void handleEviction(MainMemory mainMemory, CacheLine line, int address) {
        // Write back to memory only if line is dirty
        if (line.isDirty() && line.isValid()) {
            mainMemory.write(address, line.getData());
            line.setDirty(false);
        }
    }
}
