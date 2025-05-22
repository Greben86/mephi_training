package mephi.exercise.reactive;

public interface Observer<T> {
    Observer<T> onNext(T item);
    Observer<T> onError(Throwable t);
    void onComplete();
}
