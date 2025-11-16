package scs_project.cachememorysimulator.model;

import java.util.Arrays;

public class CacheLine {
    private int tag;
    private int[] data;
    private boolean isValid; // if cache line occupied
    private boolean isDirty; // if this line has been modified

    public CacheLine(int blockSize) {
        this.tag = -1;
        this.data = new int[blockSize];
        this.isValid = false;
        this.isDirty = false;
    }

    public void loadData(int[] block, int tag) {
        this.isValid = true;
        this.isDirty = false;
        this.tag = tag;
        System.arraycopy(block, 0, this.data, 0, this.data.length);
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        System.arraycopy(data, 0, this.data, 0, this.data.length);
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

        return String.format("Tag: %d, Data: %s, Dirty: %s", tag, Arrays.toString(data), isDirty);
    }
}