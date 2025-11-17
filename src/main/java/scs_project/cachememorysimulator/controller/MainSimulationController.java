package scs_project.cachememorysimulator.controller;

import scs_project.cachememorysimulator.model.Cache;
import scs_project.cachememorysimulator.model.CacheLine;
import scs_project.cachememorysimulator.model.MainMemory;
import scs_project.cachememorysimulator.model.Statistics;

public class MainSimulationController {
    private MainMemory memory;
    private Cache cache;
    private Statistics statistics;
    private int blockSize;

    public MainSimulationController(MainMemory memory, Cache cache, int blockSize) {
        this.memory = memory;
        this.cache = cache;
        this.blockSize = blockSize;
        this.statistics = new Statistics();
    }

    public int[] read(int address) {
        CacheLine line = cache.read(address);

        if (line != null) {
            statistics.recordHit();
            return line.getData();
        }

        statistics.recordMiss();

        int[] data = memory.read(address, blockSize);

        // 3. Install the fetched block into the cache
        //    (This handles finding a victim and writing-back if dirty)
        handleCacheInstallation(address, data, false);

        return data;
    }

    public void write(int address, int[] data) {
        // 1. Try to find the line in the cache
        CacheLine line = cache.read(address);

        if (line != null) {
            // --- CACHE HIT (Write-Back) ---
            statistics.recordHit();
            // 2. Write data ONLY to the cache
            line.setData(data);
            // 3. Mark the line as dirty
            line.setDirty(true);
        } else {
            // --- CACHE MISS (Write-Allocate) ---
            statistics.recordMiss();
            // 2. Install the new data block into the cache
            //    (This handles finding a victim and writing-back if dirty)
            handleCacheInstallation(address, data, true);
        }
    }

    /**
     * Private helper to handle placing a block in the cache on a miss.
     * This method contains the core Write-Back eviction logic.
     */
    private void handleCacheInstallation(int address, int[] data, boolean isWriteOperation) {
        // 1. Call Cache.installBlock()
        // This finds a victim line, loads the new data, and returns
        // a *copy* of the line that was evicted (if any).
        CacheLine evicted = cache.installBlock(address, data);

        // 2. Check if we evicted a valid, "dirty" line
        if (evicted != null && evicted.isValid() && evicted.isDirty()) {
            // This is the "Write-Back"
            statistics.recordDirtyEviction();

            // 3. Reconstruct the full address of the evicted block
            int setIndex = cache.getSetIndex(address); // It came from the same set
            int evictedAddress = cache.reconstructAddress(evicted.getTag(), setIndex);

            // 4. Write its data to Main Memory
            memory.write(evictedAddress, evicted.getData());
        }

        // 5. If this installation was for a Write operation,
        //    we must find the line we just installed and mark it as dirty.
        if (isWriteOperation) {
            CacheLine newlyInstalledLine = cache.read(address);
            if (newlyInstalledLine != null) {
                newlyInstalledLine.setDirty(true);
            }
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }
}