package mephi.exercise;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Observable<T> {

    private final List<Observer<T>> observerList = new LinkedList<>();

    public void subscribe(Observer<T> observer) {
        observerList.add(observer);
    }

    public <R> Observable<R> map(Function<T, R> functionMap) {
        return create();
    }

    public static <R> Observable<R> create() {
        return new Observable<>();
    }
}
