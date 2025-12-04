package scs_project.cachememorysimulator.model;

import java.util.HashMap;
import java.util.Map;

public class MainMemory {
    private Map<Integer, String> memory;
    private int size;

    public MainMemory(int size) {
        this.memory = new HashMap<>();
        this.size = size;
    }

    public String read(int address, int blockSize) {
        int blockAddress = (address / blockSize) * blockSize;
        return memory.getOrDefault(blockAddress, "Empty");
    }

    /**
     * Write the value at 'address'.
     */
    public void write(int address, String data) {
        memory.put(address, data);
    }

    public void displayMemoryState() {
        System.out.println("---------------------------------");
        System.out.println("Memory State (Non-zero blocks):");
        System.out.println("---------------------------------");

        if (memory.isEmpty()) {
            System.out.println("Memory is empty.");
        } else {
            for (Map.Entry<Integer, String> entry : memory.entrySet()) {
                System.out.println("Address: " + entry.getKey() + " | Data: " + entry.getValue());
            }
        }
    }
}
