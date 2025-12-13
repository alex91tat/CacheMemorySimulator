package scs_project.cachememorysimulator.test;

import scs_project.cachememorysimulator.model.MainMemory;

public abstract class BaseCacheTest {

    protected static final int CACHE_SIZE = 1024;
    protected static final int BLOCK_SIZE = 64;
    protected static final int MEMORY_SIZE = 4096;

    protected MainMemory initMemory() {
        MainMemory memory = new MainMemory(MEMORY_SIZE);
        for (int i = 0; i < MEMORY_SIZE; i += BLOCK_SIZE) {
            memory.write(i, "D" + i);
        }
        return memory;
    }
}
