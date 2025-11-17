package scs_project.cachememorysimulator.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainMemory {
    private Map<Integer, int[]> memory;

    public MainMemory() {
        this.memory = new HashMap<>();
    }

    public int[] read(int address, int blockSize) {
        if (memory.containsKey(address)) {
            int[] data = memory.get(address);
            return Arrays.copyOf(data, data.length);
        }

        // if the address doesn t exist yet we return zeros
        return new int[blockSize];
    }

    public void write(int address, int[] data) {
        memory.put(address, Arrays.copyOf(data, data.length));
    }

    public void displayMemoryState() {
        System.out.println("---------------------------------");
        System.out.println("Memory State (Non-zero blocks):");
        System.out.println("---------------------------------");

        if (memory.isEmpty()) {
            System.out.println("Memory is empty.");
        } else {
            for (Map.Entry<Integer, int[]> entry : memory.entrySet()) {
                System.out.println("Address: " + entry.getKey() + " | Data: " + Arrays.toString(entry.getValue()));
            }
        }
    }
}