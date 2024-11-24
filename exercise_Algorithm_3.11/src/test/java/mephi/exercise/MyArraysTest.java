package mephi.exercise;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyArraysTest {

    private final Random random = new Random();

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchByte(int capacity) {
        byte[] a = new byte[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = (byte) random.nextInt(capacity);
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchChar(int capacity) {
        char[] a = new char[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) random.nextInt(capacity);
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchDouble(int capacity) {
        double[] a = new double[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextDouble() * 100;
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchFloat(int capacity) {
        float[] a = new float[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextFloat();
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchInt(int capacity) {
        int[] a = new int[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt();
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchLong(int capacity) {
        long[] a = new long[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextLong();
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchShort(int capacity) {
        short[] a = new short[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = (short) random.nextInt(capacity);
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        int found = MyArrays.binarySearch(a, a[index]);
        assertEquals(a[index], a[found]);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 30, 50, 100, 150})
    void testBinarySearchInteger(int capacity) {
        Integer[] a = new Integer[capacity];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt();
        }
        Arrays.sort(a);
        int index = random.nextInt(capacity);
        Integer value = a[index];
        int found = MyArrays.binarySearch(a, value, Comparator.comparingInt((o) -> o));
        assertEquals(a[index], a[found]);
    }
}