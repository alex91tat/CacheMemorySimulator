package scs_project.cachememorysimulator.model;

import java.util.Arrays;

public class CacheLine {
    private int tag;
    private String data;
    private boolean isValid; // if cache line occupied
    private boolean isDirty; // if this line has been modified

    // for LRU doubly-linked list
    CacheLine prev;
    CacheLine next;

    public CacheLine() {
        this.tag = -1;
        this.data = "";
        this.isValid = false;
        this.isDirty = false;
        this.prev = null;
        this.next = null;
    }

    public void loadData(String block, int tag) {
        this.isValid = true;
        this.isDirty = false;
        this.tag = tag;
        this.data = block;
    }

    public void reset() {
        this.isValid = false;
        this.isDirty = false;
        this.tag = -1;
        this.data = "";
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.isDirty = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    @Override
    public String toString() {
        if (!isValid) {
            return "Empty CacheLine";
        }

        return String.format("Tag: %d, Data: %s, Dirty: %s", tag, data, isDirty);
    }
}