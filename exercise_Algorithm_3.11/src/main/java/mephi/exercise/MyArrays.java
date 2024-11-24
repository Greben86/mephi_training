package mephi.exercise;

import java.util.Comparator;

/**
 * Класс с реализациями бинарного поиска элемента в отсортированном массиве
 */
public final class MyArrays {

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(byte[] a, byte key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(byte[] a, int fromIndex, int toIndex, byte key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (key == a[middle]) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(char[] a, char key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(char[] a, int fromIndex, int toIndex, char key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (key == a[middle]) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(double[] a, double key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(double[] a, int fromIndex, int toIndex, double key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (Double.compare(key, a[middle]) == 0) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(float[] a, float key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(float[] a, int fromIndex, int toIndex, float key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (Float.compare(key, a[middle]) == 0) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(int[] a, int key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (key == a[middle]) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(long[] a, long key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(long[] a, int fromIndex, int toIndex, long key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (key == a[middle]) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(short[] a, short key) {
        return binarySearch(a, 0, a.length, key);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     */
    static int binarySearch(short[] a, int fromIndex, int toIndex, short key) {
        if (a == null || a.length == 0 || fromIndex >= toIndex) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (key == a[middle]) {
                return middle;
            }
            if (key > a[middle]) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению с использованием компаратора
     *
     * @param a отсортированный массив
     * @param key искомое значение
     * @param c компаратор
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     * @param <T> тип элемента массива
     */
    static <T> int binarySearch(T[] a, T key, Comparator<T> c) {
        return binarySearch(a, 0, a.length, key, c);
    }

    /**
     * Бинарный поиск индекса элемента отсортированного массива по значению с использованием компаратора
     *
     * @param a отсортированный массив
     * @param fromIndex начальный индекс области поиска
     * @param toIndex конечный индекс области поиска
     * @param key искомое значение
     * @param c компаратор
     * @return индекс элемента в массиве, если не найдено ничего, то будет возвращен -1
     * @param <T> тип элемента массива
     */
    static <T> int binarySearch(T[] a, int fromIndex, int toIndex, T key, Comparator<T> c) {
        if (a == null || a.length == 0 || fromIndex >= toIndex || c == null) {
            return -1;
        }

        int from = fromIndex;
        int to = toIndex - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            if (c.compare(key, a[middle]) == 0) {
                return middle;
            }
            if (c.compare(key, a[middle]) > 0) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }
}
