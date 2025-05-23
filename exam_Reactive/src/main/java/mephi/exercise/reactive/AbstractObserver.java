package mephi.exercise.reactive;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractObserver<T> implements Observer<T> {
    @Getter
    private final int id;
}
