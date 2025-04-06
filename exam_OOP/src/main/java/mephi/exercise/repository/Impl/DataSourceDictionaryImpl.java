package mephi.exercise.repository.Impl;

import mephi.exercise.repository.DataSource;
import mephi.exercise.repository.DataSourceDictionary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Библиотека данных
 */
public class DataSourceDictionaryImpl implements DataSourceDictionary {

    private final Map<Class<?>, DataSource<?>> map = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void putDataSource(Class<T> clazz, DataSource<T> dataSource) {
        map.putIfAbsent(clazz, dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> DataSource<T> getDataSource(Class<T> clazz) {
        return (DataSource<T>) map.get(clazz);
    }
}
