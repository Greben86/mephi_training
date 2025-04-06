package mephi.exercise.repository;

/**
 * Интерфейс библиотеки данных
 */
public interface DataSourceDictionary {

    /**
     * Добавить хранилище данных
     *
     * @param clazz тип хранимых сущностей
     * @param dataSource хранилище данных
     * @param <T> тип хранимых сущностей
     */
    <T> void putDataSource(Class<T> clazz, DataSource<T> dataSource);

    /**
     * Получить хранилище данных по типу хранимых сущностей
     *
     * @param clazz тип хранимых сущностей
     * @return хранилище данных
     * @param <T> тип хранимых сущностей
     */
    <T> DataSource<T> getDataSource(Class<T> clazz);
}
