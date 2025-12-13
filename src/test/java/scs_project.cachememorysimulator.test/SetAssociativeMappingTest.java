package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class SetAssociativeMappingTest extends BaseCacheTest {

    @Test
    void sameSetDifferentLines() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, 2,
                        new SetAssociativeMappingStrategy(BLOCK_SIZE, 8, 2),
                        new LruPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(512); // same set
        assertEquals("D0", c.read(0));
    }

    @Test
    void evictionWhenSetIsFull() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, 2,
                        new SetAssociativeMappingStrategy(BLOCK_SIZE, 8, 2),
                        new FifoPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(512);
        c.read(1024); // eviction
        assertEquals("Empty", c.read(0));
    }
}
