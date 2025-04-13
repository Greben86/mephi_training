package mephi.exercise;

public interface InterruptedSupplier<T> {

    T get(Long id) throws InterruptedException;
}
