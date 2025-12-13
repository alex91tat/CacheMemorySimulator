package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class WriteThroughTest extends BaseCacheTest {

    @Test
    void memoryUpdatedImmediately() {
        MainMemory memory = initMemory();

        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, 1,
                        new DirectMappingStrategy(BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE),
                        new LruPolicy(),
                        new WriteThroughPolicy(),
                        memory
                );

        c.read(64);
        c.write(64, "WT");
        assertEquals("WT", memory.read(64, BLOCK_SIZE));
    }

    @Test
    void noDirtyEvictionNeeded() {
        MainMemory memory = initMemory();

        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, 1,
                        new DirectMappingStrategy(BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE),
                        new LruPolicy(),
                        new WriteThroughPolicy(),
                        memory
                );

        c.read(64);
        c.write(64, "WT");
        c.read(1088); // eviction
        assertEquals("WT", memory.read(64, BLOCK_SIZE));
    }
}
