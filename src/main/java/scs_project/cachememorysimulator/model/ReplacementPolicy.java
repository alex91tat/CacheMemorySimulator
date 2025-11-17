package scs_project.cachememorysimulator.model;

public interface ReplacementPolicy {
    // for finding the line
    CacheLine findVictim(CacheSet set);

    // when a line is accessed (read or write)
    // for LRU to move line
    void onAccess(CacheSet set, CacheLine line);
}
