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


    public void write(int address, String data) {
        memory.put(address, data);
    }

    public void displayMemoryState() {
        System.out.println("---------------------------------");
        System.out.println("Memory State:");
        System.out.println("---------------------------------");

        if (memory.isEmpty()) {
            System.out.println("Memory is empty.");
        } else {
            memory.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry ->
                            System.out.println("Address: " + entry.getKey() + " | Data: " + entry.getValue())
                    );

        }
    }
}
