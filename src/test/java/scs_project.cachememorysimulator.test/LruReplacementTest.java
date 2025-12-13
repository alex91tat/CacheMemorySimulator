package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class LruReplacementTest extends BaseCacheTest {

    @Test
    void leastRecentlyUsedIsEvicted() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, 2,
                        new SetAssociativeMappingStrategy(BLOCK_SIZE, 8, 2),
                        new LruPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(512);
        c.read(0);    // make 512 LRU
        c.read(1024); // eviction
        assertEquals("D0", c.read(0));
    }

    @Test
    void recentlyUsedIsPreserved() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, 2,
                        new SetAssociativeMappingStrategy(BLOCK_SIZE, 8, 2),
                        new LruPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(512);
        c.read(512);
        c.read(1024);
        assertEquals("D512", c.read(512));
    }
}
