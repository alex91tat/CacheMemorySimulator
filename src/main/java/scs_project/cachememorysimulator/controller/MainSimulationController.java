package scs_project.cachememorysimulator.controller;

import scs_project.cachememorysimulator.model.*;

import java.util.HashMap;
import java.util.Map;

public class MainSimulationController {

    private final Cache cache;
    private final MainMemory mainMemory;
    private final Statistics statistics;
    private final WritingPolicy writingPolicy;
    private final AddressMappingStrategy mappingStrategy;
    private final Map<CacheLine, Integer> lineToAddressMap;

    public MainSimulationController(int cacheSize, int blockSize, int associativity,
                                    AddressMappingStrategy mappingStrategy,
                                    ReplacementPolicy replacementPolicy,
                                    WritingPolicy writingPolicy,
                                    MainMemory mainMemory) {

        this.mappingStrategy = mappingStrategy;
        this.writingPolicy = writingPolicy;
        this.mainMemory = mainMemory;            // ✔ Use external memory
        this.cache = new Cache(cacheSize, blockSize, associativity, mappingStrategy, replacementPolicy);
        this.statistics = new Statistics();
        this.lineToAddressMap = new HashMap<>();
    }

    private void printAddressBreakdown(int address) {

        int tag = mappingStrategy.extractTag(address);
        int index = mappingStrategy.extractIndex(address);
        int blockOffset = address % cache.getBlockSize();

        System.out.print("  Address breakdown: Tag=0x"
                + Integer.toHexString(tag).toUpperCase());

        if (mappingStrategy instanceof DirectMappingStrategy) {
            System.out.println(", Index=" + index + ", Block Offset=" + blockOffset);
        }
        else if (mappingStrategy instanceof SetAssociativeMappingStrategy) {
            System.out.println(", Set=" + index + ", Block Offset=" + blockOffset);
        }
        else if (mappingStrategy instanceof FullyAssociativeMappingStrategy) {
            System.out.println(", Block Offset=" + blockOffset);
        }
    }

    public String read(int address) {
        printAddressBreakdown(address);

        int index = mappingStrategy.extractIndex(address);
        int tag   = mappingStrategy.extractTag(address);

        CacheSet set = cache.getSets()[index];
        CacheLine line = set.findLine(tag);

        if (line != null && line.isValid()) {
            System.out.println("Cache Hit");
            statistics.recordHit();
            cache.getReplacementPolicy().onAccess(set, line);
            return line.getData();
        }

        System.out.println("Cache Miss");
        statistics.recordMiss();

        handleReadMiss(address, tag, set);

        return "Empty";
    }

    private void handleReadMiss(int address, int tag, CacheSet set) {

        CacheLine victim = cache.getReplacementPolicy().findVictim(set);

        if (victim.isValid() && victim.isDirty()) {
            statistics.recordDirtyEviction();
            Integer oldAddress = lineToAddressMap.get(victim);
            if (oldAddress != null) {
                writingPolicy.handleEviction(mainMemory, victim, oldAddress);
                lineToAddressMap.remove(victim);
            }
        }

        int blockAddress = (address / cache.getBlockSize()) * cache.getBlockSize();
        String blockData = mainMemory.read(blockAddress, cache.getBlockSize());

        int oldTag = victim.getTag();
        victim.loadData(blockData, tag);
        victim.setValid(true);
        victim.setDirty(false);

        set.updateTagMap(oldTag, tag, victim);
        lineToAddressMap.put(victim, blockAddress);

        cache.getReplacementPolicy().onAccess(set, victim);
    }


    public void write(int address, String value) {

        int index = mappingStrategy.extractIndex(address);
        int tag   = mappingStrategy.extractTag(address);

        CacheSet set = cache.getSets()[index];
        CacheLine line = set.findLine(tag);

        if (line != null && line.isValid()) {
            cache.getReplacementPolicy().onAccess(set, line);
            writingPolicy.handleWrite(mainMemory, line, address, value);
            return;
        }

        handleWriteMiss(address, value, tag, set);
    }

    private void handleWriteMiss(int address, String value, int tag, CacheSet set) {

        CacheLine victim = cache.getReplacementPolicy().findVictim(set);

        if (victim.isValid() && victim.isDirty()) {
            statistics.recordDirtyEviction();
            Integer oldAddress = lineToAddressMap.get(victim);
            if (oldAddress != null) {
                writingPolicy.handleEviction(mainMemory, victim, oldAddress);
                lineToAddressMap.remove(victim);
            }
        }

        int oldTag = victim.getTag();
        victim.loadData(value, tag);
        victim.setValid(true);

        set.updateTagMap(oldTag, tag, victim);
        lineToAddressMap.put(victim, address);

        writingPolicy.handleWrite(mainMemory, victim, address, value);
        cache.getReplacementPolicy().onAccess(set, victim);
    }


    public void displayCacheState() {
        System.out.println("Cache State:");
        for (int i = 0; i < cache.getSets().length; i++) {
            System.out.println("Set " + i + ":");
            for (CacheLine line : cache.getSets()[i].getLines()) {
                System.out.println(line);
            }
        }
    }

    public Statistics getStatistics() { return statistics; }

    public void displayMemoryState() { mainMemory.displayMemoryState(); }
}
