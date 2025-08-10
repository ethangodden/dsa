package com.github.ethangodden.datastructure;

import java.util.HashMap;
import java.util.Map;

public final class TreePartitionSet<T> implements PartitionSet<T> {
    private final Map<T, T> parentMap;
    private final Map<T, Integer> rankMap;


    public TreePartitionSet() {
        this.parentMap = new HashMap<>();
        this.rankMap = new HashMap<>();
    }

    public boolean createPartition(T e) {
        if (e == null) {
            throw new NullPointerException("null element cannot be added to a cluster");
        }

        if (parentMap.containsKey(e)) {
            return false; // Element already exists in a partition
        }

        parentMap.put(e, e);
        rankMap.put(e, 0);
        return true;
    }

    @Override
    public boolean mergePartitions(T e1, T e2) {
        T p1 = findParent(e1);
        T p2 = findParent(e2);

        if (p1.equals(p2)) {
            return false; // They are already in the same set
        }

        // Union by rank optimization
        int rank1 = rankMap.getOrDefault(p1, 0);
        int rank2 = rankMap.getOrDefault(p2, 0);

        if (rank1 < rank2) {
            parentMap.put(p1, p2);
        } else if (rank1 > rank2) {
            parentMap.put(p2, p1);
        } else {
            parentMap.put(p2, p1);
            rankMap.put(p1, rank1 + 1); // Increase the rank of the new root
        }
        return true; // Successfully merged the partitions
    }

    @Override
    public boolean samePartition(T e1, T e2) {
        return findParent(e1).equals(findParent(e2));
    }

    private T findParent(T e) {
        if (e == null) {
            throw new NullPointerException("null element cannot exist in a partition");
        }
        // Path compression
        if (!parentMap.get(e).equals(e)) {
            parentMap.put(e, findParent(parentMap.get(e)));
        }
        return parentMap.get(e);
    }

}
