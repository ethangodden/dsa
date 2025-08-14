package com.github.ethangodden.datastructure;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public final class TreePartitionSet<T> implements PartitionSet<T> {
    private final Map<T, T> parentMap;
    private final Map<T, Integer> rankMap;


    public TreePartitionSet() {
        this.parentMap = new HashMap<>();
        this.rankMap = new HashMap<>();
    }

    @Override
    public boolean createPartition(@NotNull T e) {
        Objects.requireNonNull(e, "null element cannot be added to a cluster");
        // Check if the element already exists in a partition and it was successfully added
        return !parentMap.containsKey(e) && parentMap.put(e, e) == null && rankMap.put(e, 0) == null;
    }

    @Override
    public boolean mergePartitions(@NotNull T e1, @NotNull T e2) {
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
    public boolean samePartition(@NotNull T e1, @NotNull T e2) {
        return findParent(e1).equals(findParent(e2));
    }

    private T findParent(@NotNull T e) {
        Objects.requireNonNull(e, "null element cannot exist in a partition");
        // Path compression
        if (!parentMap.get(e).equals(e)) {
            parentMap.put(e, findParent(parentMap.get(e)));
        }
        return parentMap.get(e);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return parentMap.keySet().iterator();
    }
}
