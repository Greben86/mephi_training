package mephi.exercise.repository.Impl;

import mephi.exercise.entity.User;
import mephi.exercise.repository.DataSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хранилище пользователей
 */
public class UserDataSource implements DataSource<User> {

    private Map<Integer, User> map = new ConcurrentHashMap<>();
    private AtomicInteger sequenceId = new AtomicInteger(0);

    /**
     * Конструктор с инициализацией
     *
     * @param userEntities список пользователей
     */
    public UserDataSource(List<User> userEntities) {
        userEntities.forEach(user -> map.put(user.getId(), user));
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
    public void add(Integer id, User value) {
        map.computeIfAbsent(id, s -> value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User get(Integer id) {
        return map.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
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
