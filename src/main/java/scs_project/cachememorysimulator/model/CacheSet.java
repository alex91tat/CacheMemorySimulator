package scs_project.cachememorysimulator.model;

public class CacheSet {
    private CacheLine[] lines;
    private int accessCounter; // for LRU
    private int fifoCounter; // for FIFO

    public CacheSet(int associativity, int blockSize) {
        this.lines = new CacheLine[associativity];
        for (int i = 0; i < this.lines.length; i++) {
            this.lines[i] = new CacheLine(blockSize);
        }

        this.accessCounter = 0;
        this.fifoCounter = 0;
    }

    public CacheLine findLine(int tag) {
        for (CacheLine line : this.lines) {
            if (line.getTag() == tag && line.isValid()) {
                // hit
                return line;
            }
        }

        // miss
        return null;
    }

    public CacheLine findEmptyLine() {
        for (CacheLine line : this.lines) {
            if (!line.isValid()) {
                return line;
            }
        }

        // no empty line
        return null;
    }

    public void updateLRU(CacheLine line) {
        this.accessCounter++;
        line.setLastAccessTime(this.accessCounter);
    }


    public void assignFIFOOrder(CacheLine line) {
        this.fifoCounter++;
        line.setFifoOrder(this.fifoCounter);
    }


    public CacheLine selectLineToEvict(ReplacementPolicy policy) {
        return policy.selectLine(lines);
    }

    public CacheLine[] getLines() {
        return lines;
    }

    public void setLines(CacheLine[] lines) {
        this.lines = lines;
    }

    public int getAccessCounter() {
        return accessCounter;
    }

    public void setAccessCounter(int accessCounter) {
        this.accessCounter = accessCounter;
    }

    public int getFifoCounter() {
        return fifoCounter;
    }

    public void setFifoCounter(int fifoCounter) {
        this.fifoCounter = fifoCounter;
    }
}
