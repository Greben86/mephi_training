package mephi.exercise.repository;

import java.util.List;

/**
 * Источник данных
 *
 * @param <V> тип хранимых данных
 */
public interface DataSource<V> {

    /**
     * Метод генерирует уникальный ключ (просто инкрементация счетчика как правило)
     *
     * @return уникальный ключ
     */
    Integer getNextId();

    /**
     * Метод проверяет наличие значения по ключу
     *
     * @param id уникальный ключ
     * @return если есть объект - true, иначе false
     */
    boolean contains(Integer id);

    /**
     * Метод добавляет объект в хранилище
     *
     * @param id уникальный ключ
     * @param value хранимый объект
     */
    void add(Integer id, V value);

    /**
     * Метод возвращает объект по ключу
     *
     * @param id уникальный ключ
     * @return объект, если он есть в хранилище, если нет - null
     */
    V get(Integer id);

    /**
     * Метод возвращает все объекты в хранилище
     *
     * @return коллекция объектов
     */
    List<V> getAll();

    /**
     * Метод удаляет объект по ключу
     *
     * @param id уникальный ключ
     */
    void remove(Integer id);
}
