package mephi.exercise.reactive;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Observable<T> {
    private final Consumer<Observer<T>> source;

    private final List<Observer<T>> observerList = new LinkedList<>();

    public Observable(final Consumer<Observer<T>> source) {
        this.source = source;
    }

    public static <R> Observable<R> create(final Consumer<Observer<R>> source) {
        return new Observable<>(source);
    }

    public Disposable subscribe(Observer<T> observer) {
        final var disposed = new AtomicBoolean(false);
        try {
            source.accept(new Observer<T>() {
                @Override
                public Observer<T> onNext(T item) {
                    if (!disposed.get()) {
                        observer.onNext(item);
                    }

                    return this;
                }

                @Override
                public Observer<T> onError(Throwable t) {
                    if (!disposed.get()) {
                        observer.onError(t);
                    }

                    return this;
                }

                @Override
                public void onComplete() {
                    if (!disposed.get()) {
                        observer.onComplete();
                    }
                }
            });
        } catch (Exception e) {
            if (!disposed.get()) {
                observer.onError(e);
            }
        }

        return new Disposable() {
            @Override
            public void dispose() {
                disposed.set(true);
            }

            @Override
            public boolean isDisposed() {
                return disposed.get();
            }
        };
    }

    public Disposable subscribe(Consumer<T> onNext, Consumer<Throwable> onError, Runnable onComplete) {
        return subscribe(new Observer<T>() {
            @Override
            public Observer<T> onNext(T item) {
                onNext.accept(item);

                return this;
            }

            @Override
            public Observer<T> onError(Throwable t) {
                onError.accept(t);

                return this;
            }

            @Override
            public void onComplete() {
                onComplete.run();
            }
        });
    }

    public <R> Observable<R> map(Function<T, R> functionMap) {
        return new Observable<>(observer -> subscribe(
                new Observer<>() {
                    @Override
                    public Observer<T> onNext(T item) {
                        try {
                            observer.onNext(functionMap.apply(item));
                        } catch (Exception e) {
                            observer.onError(e);
                        }
                        return this;
                    }

                    @Override
                    public Observer<T> onError(Throwable t) {
                        observer.onError(t);
                        return this;
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                }
        ));
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return new Observable<>(observer -> subscribe(
                item -> {
                    try {
                        if (predicate.test(item)) {
                            observer.onNext(item);
                        }
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                },
                observer::onError,
                observer::onComplete
        ));
    }

    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new Observable<>(observer -> {
            AtomicBoolean disposed = new AtomicBoolean(false);
            subscribe(
                    item -> {
                        if (!disposed.get()) {
                            try {
                                Observable<R> innerObservable = mapper.apply(item);
                                innerObservable.subscribe(
                                        observer::onNext,
                                        observer::onError,
                                        () -> {} // Don't complete when inner completes
                                );
                            } catch (Exception e) {
                                observer.onError(e);
                            }
                        }
                    },
                    observer::onError,
                    observer::onComplete
            );
        });
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new Observable<>(observer -> scheduler.execute(() -> subscribe(observer)));
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new Observable<>(observer -> subscribe(
                item -> scheduler.execute(() -> observer.onNext(item)),
                error -> scheduler.execute(() -> observer.onError(error)),
                () -> scheduler.execute(observer::onComplete)
        ));
    }
}
