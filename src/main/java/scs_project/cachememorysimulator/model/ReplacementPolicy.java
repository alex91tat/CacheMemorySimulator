package scs_project.cachememorysimulator.model;

public interface ReplacementPolicy {
    CacheLine selectLine(CacheLine[] lines);
}
