package com.github.ethangodden.datastructure;

public interface PartitionSet<T> {
    /**
     * Create a new partition with a single element if it is not in an existing partition.
     *
     * @param e Element to create a partition for
     * @return true if the partition was created, false if it already exists
     * @throws NullPointerException if the element is null
     */
    boolean createPartition(T e);

    /**
     * Merge the partitions containing the two elements.
     *
     * @param e1 First element
     * @param e2 Second element
     * @return true if the partitions were merged, false if they were already in the same partition
     * @throws NullPointerException if either element is null
     */
    boolean mergePartitions(T e1, T e2);

    /**
     * Check if two elements are in the same partition.
     *
     * @param e1 First element
     * @param e2 Second element
     * @return true if both elements are in the same partition, false otherwise
     * @throws NullPointerException if either element is null
     */
    boolean samePartition(T e1, T e2);
}

