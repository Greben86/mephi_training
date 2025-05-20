package mephi.exercise.pool;

@FunctionalInterface
public interface InterruptedSupplier<T> {

    T get(int id) throws InterruptedException;
}
