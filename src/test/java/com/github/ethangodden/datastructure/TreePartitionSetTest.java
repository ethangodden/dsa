package com.github.ethangodden.datastructure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreePartitionSetTest extends PartitionSetTest<Integer> {
    
    @Override
    protected PartitionSet<Integer> createPartitionSet() {
        return new TreePartitionSet<>();
    }
    
    @Override
    protected Integer createElement(int index) {
        return index;
    }
    
    @Test
    public void testStringElements() {
        TreePartitionSet<String> stringPartitionSet = new TreePartitionSet<>();
        
        assertTrue(stringPartitionSet.createPartition("apple"));
        assertTrue(stringPartitionSet.createPartition("banana"));
        assertTrue(stringPartitionSet.createPartition("cherry"));
        
        assertFalse(stringPartitionSet.samePartition("apple", "banana"));
        
        assertTrue(stringPartitionSet.mergePartitions("apple", "banana"));
        assertTrue(stringPartitionSet.samePartition("apple", "banana"));
        
        assertFalse(stringPartitionSet.mergePartitions("apple", "banana"));
        
        assertTrue(stringPartitionSet.mergePartitions("banana", "cherry"));
        assertTrue(stringPartitionSet.samePartition("apple", "cherry"));
    }
    
    @Test
    public void testCustomObjectElements() {
        TreePartitionSet<TestObject> objectPartitionSet = new TreePartitionSet<>();
        
        TestObject obj1 = new TestObject(1, "Object1");
        TestObject obj2 = new TestObject(2, "Object2");
        TestObject obj3 = new TestObject(3, "Object3");
        
        assertTrue(objectPartitionSet.createPartition(obj1));
        assertTrue(objectPartitionSet.createPartition(obj2));
        assertTrue(objectPartitionSet.createPartition(obj3));
        
        assertFalse(objectPartitionSet.samePartition(obj1, obj2));
        
        assertTrue(objectPartitionSet.mergePartitions(obj1, obj2));
        assertTrue(objectPartitionSet.samePartition(obj1, obj2));
        
        assertTrue(objectPartitionSet.mergePartitions(obj2, obj3));
        assertTrue(objectPartitionSet.samePartition(obj1, obj3));
    }
    
    @Test
    public void testLargeNumberOfElements() {
        TreePartitionSet<Integer> largeSet = new TreePartitionSet<>();
        int n = 1000;
        
        for (int i = 0; i < n; i++) {
            assertTrue(largeSet.createPartition(i));
        }
        
        for (int i = 0; i < n - 1; i += 2) {
            assertTrue(largeSet.mergePartitions(i, i + 1));
        }
        
        for (int i = 0; i < n - 1; i += 2) {
            assertTrue(largeSet.samePartition(i, i + 1));
            if (i + 2 < n) {
                assertFalse(largeSet.samePartition(i, i + 2));
            }
        }
        
        for (int i = 0; i < n - 2; i += 4) {
            if (i + 2 < n) {
                assertTrue(largeSet.mergePartitions(i, i + 2));
            }
        }
        
        for (int i = 0; i < n - 3; i += 4) {
            assertTrue(largeSet.samePartition(i, i + 1));
            assertTrue(largeSet.samePartition(i, i + 2));
            assertTrue(largeSet.samePartition(i, i + 3));
            if (i + 4 < n) {
                assertFalse(largeSet.samePartition(i, i + 4));
            }
        }
    }
    
    @Test
    public void testPathCompression() {
        TreePartitionSet<Integer> set = new TreePartitionSet<>();
        
        for (int i = 0; i < 10; i++) {
            set.createPartition(i);
        }
        
        for (int i = 0; i < 9; i++) {
            set.mergePartitions(i, i + 1);
        }
        
        assertTrue(set.samePartition(0, 9));
        assertTrue(set.samePartition(3, 7));
        assertTrue(set.samePartition(1, 8));
    }
    
    private static class TestObject {
        private final int id;
        private final String name;
        
        public TestObject(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return id == that.id;
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
        
        @Override
        public String toString() {
            return "TestObject{id=" + id + ", name='" + name + "'}";
        }
    }
}