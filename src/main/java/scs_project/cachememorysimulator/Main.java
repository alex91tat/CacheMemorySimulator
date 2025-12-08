package scs_project.cachememorysimulator;

import scs_project.cachememorysimulator.controller.MainSimulationController;
import scs_project.cachememorysimulator.model.*;

public class Main {
    public static void main(String[] args) {
        int cacheSize = 1024;    // 1KB cache
        int blockSize = 64;      // 64B blocks
        int memorySize = 65536;  // 64KB main memory

        MainMemory mainMemory = new MainMemory(memorySize);
        for (int i = 0; i < 2000; i += 64) {
            String blockData = String.format("D%d", i);
            mainMemory.write(i, blockData);
        }


        System.out.println("=" + "=".repeat(49));
        System.out.println("DIRECT MAPPING");
        System.out.println("=" + "=".repeat(49));
        System.out.println("Cache: " + cacheSize + " bytes, Block: " + blockSize + " bytes");

        DirectMappingStrategy directMapping = new DirectMappingStrategy(blockSize, cacheSize/blockSize);
        LruPolicy lruPolicy = new LruPolicy();
        WriteBackPolicy writeBackPolicy = new WriteBackPolicy();

        MainSimulationController directController = new MainSimulationController(
                cacheSize, blockSize, 1, directMapping, lruPolicy, writeBackPolicy, mainMemory
        );

        int[] directAddresses = {0, 64, 70, 0, 256, 64, 1024};
        for (int addr : directAddresses) {
            System.out.println("\nAccessing address: " + addr + " (0x" + Integer.toHexString(addr) + ")");
            String data = directController.read(addr);
            System.out.println("Read value: " + data);
        }
        System.out.println();

        directController.displayCacheState();
        System.out.println("\nDirect Mapping Statistics:");
        System.out.println(directController.getStatistics());

        System.out.println("\n\n" + "=" + "=".repeat(49));
        System.out.println("FULLY ASSOCIATIVE MAPPING");
        System.out.println("=" + "=".repeat(49));
        System.out.println("Cache: " + cacheSize + " bytes, Block: " + blockSize + " bytes");

        int totalLines = cacheSize / blockSize;
        FullyAssociativeMappingStrategy fullyAssociative = new FullyAssociativeMappingStrategy(blockSize);
        FifoPolicy fifoPolicy = new FifoPolicy();

        MainSimulationController fullyAssociativeController = new MainSimulationController(
            cacheSize, blockSize, totalLines, fullyAssociative, fifoPolicy, writeBackPolicy, mainMemory
        );

        int[] fullyAssociativeAddresses = {0, 64, 128, 192, 256, 320, 384, 448, 512, 576, 640, 704, 
                                         768, 832, 896, 960, 128, 384, 0};
        
        for (int addr : fullyAssociativeAddresses) {
            System.out.println("\nAccessing address: " + addr + " (0x" + Integer.toHexString(addr) + ")");
            String data = fullyAssociativeController.read(addr);
            System.out.println("Read value: " + data);
        }

        fullyAssociativeController.write(0, "D999");
        System.out.println("\nAccessing address: 1024 (0x400)");
        fullyAssociativeController.read(1024);

        fullyAssociativeController.displayCacheState();
        fullyAssociativeController.displayMemoryState();

        System.out.println("\n\n" + "=" + "=".repeat(49));
        System.out.println("SET-ASSOCIATIVE MAPPING");
        System.out.println("=" + "=".repeat(49));
        System.out.println("Cache: " + cacheSize + " bytes, Block: " + blockSize + " bytes");

        int associativity = 2;  // 2-way set associative
        int numberOfSets = cacheSize / (blockSize * associativity);
        SetAssociativeMappingStrategy setAssociative = new SetAssociativeMappingStrategy(
            blockSize, numberOfSets, associativity);

        MainSimulationController setAssociativeController = new MainSimulationController(
            cacheSize, blockSize, associativity, setAssociative, fifoPolicy, writeBackPolicy, mainMemory
        );

        int[] setAssociativeAddresses = {0, 512, 1024, 64, 128, 64};
        
        for (int addr : setAssociativeAddresses) {
            System.out.println("\nAccessing address: " + addr + " (0x" + Integer.toHexString(addr) + ")");
            String data = setAssociativeController.read(addr);
            System.out.println("Read value: " + data);
        }

        setAssociativeController.displayCacheState();
        System.out.println("\nSet Associative Statistics:");
        System.out.println(setAssociativeController.getStatistics());

        System.out.println("\n===========================================");
        System.out.println("WRITE-BACK POLICY TEST");
        System.out.println("===========================================");

        WriteBackPolicy wb = new WriteBackPolicy();
        DirectMappingStrategy dmWB = new DirectMappingStrategy(blockSize, cacheSize / blockSize);

        MainSimulationController wbController = new MainSimulationController(
                cacheSize, blockSize, 1, dmWB, new LruPolicy(), wb, mainMemory);

        wbController.read(0);
        wbController.write(0, "D_modified");

        System.out.println("Main Memory at 0: " + mainMemory.read(0, blockSize));
        wbController.read(1024);

        System.out.println("Memory at 0 after eviction: " + mainMemory.read(0, blockSize));



        System.out.println("\n===========================================");
        System.out.println("WRITE-THROUGH POLICY TEST");
        System.out.println("===========================================");

        WriteThroughPolicy wt = new WriteThroughPolicy();
        DirectMappingStrategy dmWT = new DirectMappingStrategy(blockSize, cacheSize / blockSize);

        MainSimulationController wtController = new MainSimulationController(
                cacheSize, blockSize, 1, dmWT, new LruPolicy(), wt, mainMemory
        );

        wtController.read(64);

        wtController.write(64, "D_data");

        System.out.println("Main Memory at 64 (should be WT_DATA): " + mainMemory.read(64, blockSize));
    }
}