package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class FullyAssociativeMappingTest extends BaseCacheTest {

    @Test
    void anyBlockCanBePlacedAnywhere() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE,
                        new FullyAssociativeMappingStrategy(BLOCK_SIZE),
                        new FifoPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        assertEquals("Empty", c.read(128));
        assertEquals("D128", c.read(128));
    }

    @Test
    void hitAfterMultipleLoads() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE, BLOCK_SIZE, CACHE_SIZE / BLOCK_SIZE,
                        new FullyAssociativeMappingStrategy(BLOCK_SIZE),
                        new FifoPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(64);
        c.read(128);
        assertEquals("D64", c.read(64));
    }
}