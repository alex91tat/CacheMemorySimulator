package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class WriteBackTest extends BaseCacheTest {

    @Test
    void memoryUpdatedOnlyOnEviction() {
        MainMemory memory = initMemory();

        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE,
                        BLOCK_SIZE,
                        1,
                        new DirectMappingStrategy(BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE),
                        new LruPolicy(),
                        new WriteBackPolicy(),
                        memory
                );

        c.read(0);
        c.write(0, "MOD");

        // Should NOT be updated yet
        assertEquals("D0", memory.read(0, BLOCK_SIZE));

        // Cause eviction
        c.read(1024);

        // Now must be updated
        assertEquals("MOD", memory.read(0, BLOCK_SIZE));
    }
}
