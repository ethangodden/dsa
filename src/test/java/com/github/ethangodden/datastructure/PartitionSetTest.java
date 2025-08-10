package com.github.ethangodden.datastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public abstract class PartitionSetTest<T> {
    
    protected PartitionSet<T> partitionSet;
    
    protected abstract PartitionSet<T> createPartitionSet();
    protected abstract T createElement(int index);
    
    @BeforeEach
    public void setUp() {
        partitionSet = createPartitionSet();
    }
    
    @Test
    public void testCreatePartition() {
        T element1 = createElement(1);
        T element2 = createElement(2);
        
        assertTrue(partitionSet.createPartition(element1));
        assertFalse(partitionSet.createPartition(element1));
        
        assertTrue(partitionSet.createPartition(element2));
        assertFalse(partitionSet.createPartition(element2));
    }
    
    @Test
    public void testCreatePartitionWithNull() {
        assertThrows(NullPointerException.class, () -> {
            partitionSet.createPartition(null);
        });
    }
    
    @Test
    public void testSamePartitionInitially() {
        T element1 = createElement(1);
        T element2 = createElement(2);
        
        partitionSet.createPartition(element1);
        partitionSet.createPartition(element2);
        
        assertTrue(partitionSet.samePartition(element1, element1));
        assertTrue(partitionSet.samePartition(element2, element2));
        assertFalse(partitionSet.samePartition(element1, element2));
    }
    
    @Test
    public void testMergePartitions() {
        T element1 = createElement(1);
        T element2 = createElement(2);
        T element3 = createElement(3);
        
        partitionSet.createPartition(element1);
        partitionSet.createPartition(element2);
        partitionSet.createPartition(element3);
        
        assertFalse(partitionSet.samePartition(element1, element2));
        
        assertTrue(partitionSet.mergePartitions(element1, element2));
        
        assertTrue(partitionSet.samePartition(element1, element2));
        assertFalse(partitionSet.samePartition(element1, element3));
        
        assertFalse(partitionSet.mergePartitions(element1, element2));
    }
    
    @Test
    public void testMergePartitionsWithNull() {
        T element1 = createElement(1);
        partitionSet.createPartition(element1);
        
        assertThrows(NullPointerException.class, () -> {
            partitionSet.mergePartitions(null, element1);
        });
        
        assertThrows(NullPointerException.class, () -> {
            partitionSet.mergePartitions(element1, null);
        });
        
        assertThrows(NullPointerException.class, () -> {
            partitionSet.mergePartitions(null, null);
        });
    }
    
    @Test
    public void testSamePartitionWithNull() {
        T element1 = createElement(1);
        partitionSet.createPartition(element1);
        
        assertThrows(NullPointerException.class, () -> {
            partitionSet.samePartition(null, element1);
        });
        
        assertThrows(NullPointerException.class, () -> {
            partitionSet.samePartition(element1, null);
        });
        
        assertThrows(NullPointerException.class, () -> {
            partitionSet.samePartition(null, null);
        });
    }
    
    @Test
    public void testTransitiveMerge() {
        T element1 = createElement(1);
        T element2 = createElement(2);
        T element3 = createElement(3);
        T element4 = createElement(4);
        
        partitionSet.createPartition(element1);
        partitionSet.createPartition(element2);
        partitionSet.createPartition(element3);
        partitionSet.createPartition(element4);
        
        partitionSet.mergePartitions(element1, element2);
        partitionSet.mergePartitions(element2, element3);
        
        assertTrue(partitionSet.samePartition(element1, element3));
        assertFalse(partitionSet.samePartition(element1, element4));
        
        partitionSet.mergePartitions(element3, element4);
        
        assertTrue(partitionSet.samePartition(element1, element4));
        assertTrue(partitionSet.samePartition(element2, element4));
    }
    
    @Test
    public void testComplexMergeScenario() {
        // Create individual elements to avoid unsafe cast
        T e0 = createElement(0);
        T e1 = createElement(1);
        T e2 = createElement(2);
        T e3 = createElement(3);
        T e4 = createElement(4);
        T e5 = createElement(5);
        T e6 = createElement(6);
        T e7 = createElement(7);
        T e8 = createElement(8);
        T e9 = createElement(9);
        
        // Create partitions for all elements
        partitionSet.createPartition(e0);
        partitionSet.createPartition(e1);
        partitionSet.createPartition(e2);
        partitionSet.createPartition(e3);
        partitionSet.createPartition(e4);
        partitionSet.createPartition(e5);
        partitionSet.createPartition(e6);
        partitionSet.createPartition(e7);
        partitionSet.createPartition(e8);
        partitionSet.createPartition(e9);
        
        // Merge some partitions
        partitionSet.mergePartitions(e0, e1);
        partitionSet.mergePartitions(e2, e3);
        partitionSet.mergePartitions(e0, e3);
        
        // Verify first group of merges
        assertTrue(partitionSet.samePartition(e0, e1));
        assertTrue(partitionSet.samePartition(e0, e2));
        assertTrue(partitionSet.samePartition(e0, e3));
        assertTrue(partitionSet.samePartition(e1, e2));
        assertTrue(partitionSet.samePartition(e1, e3));
        assertTrue(partitionSet.samePartition(e2, e3));
        
        // Verify elements not in the merged partition
        assertFalse(partitionSet.samePartition(e0, e4));
        assertFalse(partitionSet.samePartition(e1, e5));
        
        // Create another merged group
        partitionSet.mergePartitions(e5, e6);
        partitionSet.mergePartitions(e6, e7);
        partitionSet.mergePartitions(e7, e8);
        
        // Verify second group of merges
        assertTrue(partitionSet.samePartition(e5, e8));
        
        // Merge the two groups
        partitionSet.mergePartitions(e0, e8);
        
        // Verify the groups are merged
        assertTrue(partitionSet.samePartition(e0, e5));
        assertTrue(partitionSet.samePartition(e3, e8));
    }
}