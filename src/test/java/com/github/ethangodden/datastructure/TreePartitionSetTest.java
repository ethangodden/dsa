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
        
        // Create n partitions
        for (int i = 0; i < n; i++) {
            assertTrue(largeSet.createPartition(i));
        }
        
        // First merge: pair adjacent elements (0-1, 2-3, 4-5, ...)
        for (int i = 0; i < n - 1; i += 2) {
            assertTrue(largeSet.mergePartitions(i, i + 1));
        }
        
        // Verify first merge: elements in pairs are in same partition
        for (int i = 0; i < n - 1; i += 2) {
            assertTrue(largeSet.samePartition(i, i + 1), "Elements " + i + " and " + (i + 1) + " should be in same partition");
            
            // Elements from different pairs should not be in same partition
            if (i + 2 < n) {
                assertFalse(largeSet.samePartition(i, i + 2), "Elements " + i + " and " + (i + 2) + " should not be in same partition yet");
            }
        }
        
        // Second merge: merge pairs to form groups of 4 (0-1-2-3, 4-5-6-7, ...)
        for (int i = 0; i < n - 3; i += 4) {
            assertTrue(largeSet.mergePartitions(i, i + 2));
        }
        
        // Verify second merge: all elements in groups of 4 are in same partition
        for (int i = 0; i < n - 3; i += 4) {
            assertTrue(largeSet.samePartition(i, i + 1));
            assertTrue(largeSet.samePartition(i, i + 2));
            assertTrue(largeSet.samePartition(i, i + 3));
            
            // Elements from different groups should not be in same partition
            if (i + 4 < n) {
                assertFalse(largeSet.samePartition(i, i + 4), "Elements " + i + " and " + (i + 4) + " should not be in same partition");
            }
        }
    }
    
    /**
     * This test creates a chain of elements and verifies that all elements
     * are correctly identified as being in the same partition.
     * 
     * Note: This doesn't directly test the path compression implementation,
     * but rather tests the outcome that all elements are correctly identified
     * as being in the same partition after merging.
     * 
     * A proper test of path compression would require access to the internal
     * structure or measuring performance characteristics.
     */
    @Test
    public void testLongChainPartition() {
        TreePartitionSet<Integer> set = new TreePartitionSet<>();
        int n = 100; // Using a larger chain to better demonstrate the scenario
        
        // Create n separate partitions
        for (int i = 0; i < n; i++) {
            set.createPartition(i);
        }
        
        // Create a chain by merging consecutive elements
        for (int i = 0; i < n - 1; i++) {
            set.mergePartitions(i, i + 1);
        }
        
        // Verify that elements at the ends of the chain are in the same partition
        assertTrue(set.samePartition(0, n - 1), 
            "First and last elements should be in the same partition");
        
        // Verify that arbitrary elements in the chain are in the same partition
        assertTrue(set.samePartition(n/4, 3*n/4), 
            "Elements at 1/4 and 3/4 positions should be in the same partition");
        
        // Verify that all elements are in the same partition
        for (int i = 0; i < n; i++) {
            assertTrue(set.samePartition(0, i), 
                "Element 0 and " + i + " should be in the same partition");
        }
    }
    
    /**
     * Test behavior when trying to merge partitions with elements that don't exist
     * in any partition yet.
     */
    @Test
    public void testMergeNonExistentElements() {
        TreePartitionSet<Integer> set = new TreePartitionSet<>();
        
        // Create one partition
        set.createPartition(1);
        
        // Try to merge with an element that doesn't exist
        try {
            set.mergePartitions(1, 2);
            fail("Should throw exception when merging with non-existent element");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
        
        // Try to merge two elements that don't exist
        try {
            set.mergePartitions(2, 3);
            fail("Should throw exception when merging two non-existent elements");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
    }
    
    /**
     * Test behavior when checking if elements are in the same partition
     * when one or both elements don't exist in any partition.
     */
    @Test
    public void testSamePartitionNonExistentElements() {
        TreePartitionSet<Integer> set = new TreePartitionSet<>();
        
        // Create one partition
        set.createPartition(1);
        
        // Try to check same partition with one element that doesn't exist
        try {
            set.samePartition(1, 2);
            fail("Should throw exception when checking with non-existent element");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
        
        // Try to check same partition with two elements that don't exist
        try {
            set.samePartition(2, 3);
            fail("Should throw exception when checking two non-existent elements");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
    }
    
    /**
     * Test behavior when accessing non-existent elements through the public methods.
     * This indirectly tests the private findParent method with non-existent elements.
     */
    @Test
    public void testNonExistentElementAccess() {
        TreePartitionSet<Integer> set = new TreePartitionSet<>();
        
        // Test with empty set
        try {
            set.samePartition(1, 2);
            fail("Should throw exception when accessing non-existent elements in empty set");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
        
        // Add one element
        set.createPartition(1);
        
        // Test with one element that exists and one that doesn't
        try {
            set.samePartition(1, 2);
            fail("Should throw exception when one element doesn't exist");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
        
        try {
            set.mergePartitions(1, 2);
            fail("Should throw exception when merging with non-existent element");
        } catch (IllegalArgumentException | NullPointerException e) {
            // Expected exception
        }
    }
    
    /**
     * Test the rank-based union optimization by creating two trees of different heights
     * and verifying that merging them results in the expected structure.
     * 
     * We can't directly test the internal implementation, but we can verify that
     * all elements are correctly identified as being in the same partition after merging.
     */
    @Test
    public void testRankBasedUnion() {
        TreePartitionSet<Integer> set = new TreePartitionSet<>();
        
        // Create two trees with different heights
        
        // First tree: a balanced tree with 7 elements (height 2)
        // Structure:      1
        //               /   \
        //              2     3
        //             / \   / \
        //            4   5 6   7
        for (int i = 1; i <= 7; i++) {
            set.createPartition(i);
        }
        
        // Create the balanced tree structure
        set.mergePartitions(1, 2);
        set.mergePartitions(1, 3);
        set.mergePartitions(2, 4);
        set.mergePartitions(2, 5);
        set.mergePartitions(3, 6);
        set.mergePartitions(3, 7);
        
        // Second tree: a linear chain with 3 elements (height 2)
        // Structure: 8 - 9 - 10
        for (int i = 8; i <= 10; i++) {
            set.createPartition(i);
        }
        
        // Create the chain structure
        set.mergePartitions(8, 9);
        set.mergePartitions(9, 10);
        
        // Merge the two trees
        set.mergePartitions(1, 8);
        
        // Verify that all elements are in the same partition
        for (int i = 1; i <= 10; i++) {
            for (int j = i + 1; j <= 10; j++) {
                assertTrue(set.samePartition(i, j),
                    "Elements " + i + " and " + j + " should be in the same partition after merging");
            }
        }
    }

    private record TestObject(int id, String name) {

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