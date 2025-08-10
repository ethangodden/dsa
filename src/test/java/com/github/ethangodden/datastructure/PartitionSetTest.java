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
        T[] elements = (T[]) new Object[10];
        for (int i = 0; i < 10; i++) {
            elements[i] = createElement(i);
            partitionSet.createPartition(elements[i]);
        }
        
        partitionSet.mergePartitions(elements[0], elements[1]);
        partitionSet.mergePartitions(elements[2], elements[3]);
        partitionSet.mergePartitions(elements[0], elements[3]);
        
        assertTrue(partitionSet.samePartition(elements[0], elements[1]));
        assertTrue(partitionSet.samePartition(elements[0], elements[2]));
        assertTrue(partitionSet.samePartition(elements[0], elements[3]));
        assertTrue(partitionSet.samePartition(elements[1], elements[2]));
        assertTrue(partitionSet.samePartition(elements[1], elements[3]));
        assertTrue(partitionSet.samePartition(elements[2], elements[3]));
        
        assertFalse(partitionSet.samePartition(elements[0], elements[4]));
        assertFalse(partitionSet.samePartition(elements[1], elements[5]));
        
        partitionSet.mergePartitions(elements[5], elements[6]);
        partitionSet.mergePartitions(elements[6], elements[7]);
        partitionSet.mergePartitions(elements[7], elements[8]);
        
        assertTrue(partitionSet.samePartition(elements[5], elements[8]));
        
        partitionSet.mergePartitions(elements[0], elements[8]);
        
        assertTrue(partitionSet.samePartition(elements[0], elements[5]));
        assertTrue(partitionSet.samePartition(elements[3], elements[8]));
    }
}