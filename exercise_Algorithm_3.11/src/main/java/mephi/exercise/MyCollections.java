package mephi.exercise;

import java.util.Comparator;
import java.util.List;

/**
 * Класс с реализациями бинарного поиска элемента в отсортированной коллекции
 */
public class MyCollections {

    /**
     * Бинарный поиск индекса элемента отсортированной коллекции по значению
     *
     * @param list отсортированная коллекция
     * @param key искомое значение
     * @return индекс элемента в коллекции, если не найдено ничего, то будет возвращен -1
     * @param <T> тип элемента коллекции
     */
    static <T extends Comparable<? super T>> int binarySearch(List<T> list, T key) {
        if (list == null || list.isEmpty()) {
            return -1;
        }

        return binarySearch(list, key, Comparator.naturalOrder());
    }

    /**
     * Бинарный поиск индекса элемента отсортированной коллекции по значению
     *
     * @param list отсортированная коллекция
     * @param key искомое значение
     * @param c компаратор
     * @return индекс элемента в коллекции, если не найдено ничего, то будет возвращен -1
     * @param <T> тип элемента коллекции
     */
    static <T> int binarySearch(List<T> list, T key, Comparator<T> c) {
        if (list == null || list.isEmpty() || c == null) {
            return -1;
        }

        int from = 0;
        int to = list.size() - 1;
        while (from <= to) {
            int middle = ((to - from) / 2) + from;
            T value = list.get(middle);
            if (c.compare(key, value) == 0) {
                return middle;
            }
            if (c.compare(key, value) > 0) {
                from = middle + 1;
            } else {
                to = middle - 1;
            }
        }

        return -1;
    }
}
