package mephi.exercise.pool;

public interface InterruptedSupplier<T> {

    T get(int id) throws InterruptedException;
}
