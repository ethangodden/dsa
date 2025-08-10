package com.github.ethangodden.datastructure;

/**
 * Tree-based implementation of Disjoint Set (Union-Find) data structure
 * with path compression and union by rank optimizations.
 */
public class IntRangePartitionSet {
    private final int[] parent;
    private final int[] rank;
    private int numSets;

    /**
     * Initialize a disjoint set with n elements (0 to n-1)
     * @param n Number of elements
     */
    public IntRangePartitionSet(int n) {
        parent = new int[n];
        rank = new int[n];
        numSets = n;

        // Initially, each element is its own parent (self-loop)
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Find the representative (root) of the set containing element x
     * Uses path compression for optimization
     * @param x Element to find
     * @return Representative of the set containing x
     */
    public int find(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IllegalArgumentException("Element out of bounds");
        }

        // Path compression: Make all nodes point directly to root
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * Union two sets containing elements x and y
     * Uses union by rank for optimization
     * @param x First element
     * @param y Second element
     * @return true if union was performed, false if already in same set
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        // Already in the same set
        if (rootX == rootY) {
            return false;
        }

        // Union by rank: Attach smaller tree under larger tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // Equal ranks: attach y under x and increment rank
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        numSets--;
        return true;
    }



}