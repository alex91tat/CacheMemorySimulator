package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class FifoReplacementTest extends BaseCacheTest {

    @Test
    void firstInsertedIsEvicted() {
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
        c.read(1024); // evicts 0
        assertEquals("Empty", c.read(0));
    }

    @Test
    void accessDoesNotAffectOrder() {
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
        c.read(0);    // FIFO ignores access
        c.read(1024);
        assertEquals("Empty", c.read(0));
    }
}