package mephi.exercise.repository.Impl;

import mephi.exercise.entity.Category;
import mephi.exercise.repository.DataSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хранилище категорий расходов
 */
public class CategoryDataSource implements DataSource<Category> {

    private Map<Integer, Category> map = new ConcurrentHashMap<>();
    private AtomicInteger sequenceId = new AtomicInteger(0);

    /**
     * Конструктор с инициализацией
     *
     * @param categories список категорий
     */
    public CategoryDataSource(List<Category> categories) {
        categories.forEach(category -> map.put(category.getId(), category));
        sequenceId.set(map.keySet().stream()
                .mapToInt(id -> id)
                .max()
                .orElse(sequenceId.get()));
    }

    @Override
    public Integer getNextId() {
        return sequenceId.incrementAndGet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Integer id) {
        return map.containsKey(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Integer id, Category value) {
        map.computeIfAbsent(id, s -> value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category get(Integer id) {
        return map.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getAll() {
        return map.values().stream().toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void remove(Integer id) {
        map.remove(id);
    }
}
