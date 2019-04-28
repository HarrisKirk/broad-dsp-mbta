package com.harriskirk.jobhunt.broad.mbta

public class Route implements Comparable {
    String longName
    String id
    List stops

    String toString() {
        return id
    }

    public int compareTo(Object other) {
        if (this.id < other.id) {
            return -1;
        }
        if (this.id == other.id) {
            return 0;
        }
        return 1;
    }
    
}
