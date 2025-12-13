package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class DirectMappingTest extends BaseCacheTest {

    @Test
    void missThenHitSameBlock() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE,
                        BLOCK_SIZE,
                        1,
                        new DirectMappingStrategy(BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE),
                        new LruPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        // First access → miss
        assertEquals("Empty", c.read(64));

        // Same block → hit
        assertEquals("D64", c.read(70));
    }

    @Test
    void conflictEvictionOccurs() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE,
                        BLOCK_SIZE,
                        1,
                        new DirectMappingStrategy(BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE),
                        new LruPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(1024); // same index → eviction

        assertEquals("Empty", c.read(0));
    }
}
