package scs_project.cachememorysimulator.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class RandomReplacementTest extends BaseCacheTest {

    @Test
    void evictionOccursWhenSetIsFull() {
        MainSimulationController c =
                new MainSimulationController(
                        CACHE_SIZE,
                        BLOCK_SIZE,
                        2,
                        new SetAssociativeMappingStrategy(BLOCK_SIZE, 8, 2),
                        new RandomPolicy(),
                        new WriteBackPolicy(),
                        initMemory()
                );

        c.read(0);
        c.read(512);
        c.read(1024);

        String r0 = c.read(0);
        String r512 = c.read(512);

        assertTrue(
                r0.equals("Empty") || r512.equals("Empty")
        );
    }
}
