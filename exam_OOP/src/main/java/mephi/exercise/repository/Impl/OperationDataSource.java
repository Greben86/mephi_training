package mephi.exercise.repository.Impl;

import mephi.exercise.entity.Operation;
import mephi.exercise.repository.DataSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хранилище операций расходов
 */
public class OperationDataSource implements DataSource<Operation> {

    private Map<Integer, Operation> map = new ConcurrentHashMap<>();
    private AtomicInteger sequenceId = new AtomicInteger(0);

    /**
     * Конструктор с инициализацией
     *
     * @param operations список операций
     */
    public OperationDataSource(List<Operation> operations) {
        operations.forEach(category -> map.put(category.getId(), category));
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
    public void add(Integer id, Operation value) {
        map.computeIfAbsent(id, s -> value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Operation get(Integer id) {
        return map.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Operation> getAll() {
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
