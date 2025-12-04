package scs_project.cachememorysimulator.model;

import java.util.HashMap;
import java.util.LinkedList;

public class CacheSet {
    private CacheLine[] lines;

    // for LRU
    private CacheLine head; // MRU
    private CacheLine tail; // LRU
    private HashMap<Integer, CacheLine> tagMap;

    public CacheSet(int associativity, int blockSize) {
        this.lines = new CacheLine[associativity];
        for (int i = 0; i < this.lines.length; i++) {
            this.lines[i] = new CacheLine();
        }

        this.head = null;
        this.tail = null;
        this.tagMap = new HashMap<>();
    }

    public CacheLine findLine(int tag) {
        return tagMap.get(tag);
    }

    public void moveToHead(CacheLine line) {
        if (line == head)
            return;

        if (line.prev != null)
            line.prev.next = line.next;
        if (line.next != null)
            line.next.prev = line.prev;

        if (line == tail && line.prev != null)
            tail = line.prev;

        line.prev = null;
        line.next = head;

        if (head != null) {
            head.prev = line;
        }
        head = line;

        if (tail == null) {
            tail = line;
        }
    }

    public void updateTagMap(int oldTag, int newTag, CacheLine line) {
        if (oldTag != -1) {
            tagMap.remove(oldTag);
        }
        tagMap.put(newTag, line);
    }

    public CacheLine[] getLines() {
        return lines;
    }

    public void setLines(CacheLine[] lines) {
        this.lines = lines;
    }

    public CacheLine getHead() {
        return head;
    }

    public void setHead(CacheLine head) {
        this.head = head;
    }

    public CacheLine getTail() {
        return tail;
    }

    public void setTail(CacheLine tail) {
        this.tail = tail;
    }
}
