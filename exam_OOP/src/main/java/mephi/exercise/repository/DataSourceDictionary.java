package mephi.exercise.repository;

public interface DataSourceDictionary {

    <T> void putDataSource(Class<T> clazz, DataSource<T> dataSource);

    <T> DataSource<T> getDataSource(Class<T> clazz);
}
