package mephi.exercise;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyCollectionsTest {

    private final Random random = new Random();

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150, 1000, 10_000, 1000_000})
    void testBinarySearch(int capacity) {
        List<Long> a = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            a.add(random.nextLong());
        }
        Long value = a.get(random.nextInt(capacity));
        Collections.sort(a);

        assertEquals(Collections.binarySearch(a, value), MyCollections.binarySearch(a, value));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150, 1000, 10_000, 1000_000})
    void testBinarySearchWithComparator(int capacity) {
        List<Long> a = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            a.add(random.nextLong());
        }
        Long value = a.get(random.nextInt(capacity));
        Collections.sort(a);

        assertEquals(Collections.binarySearch(a, value, Comparator.comparingLong((o) -> o)),
                MyCollections.binarySearch(a, value, Comparator.comparingLong((o) -> o)));
    }
}