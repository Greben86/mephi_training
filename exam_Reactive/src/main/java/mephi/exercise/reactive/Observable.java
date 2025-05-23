package mephi.exercise.reactive;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mephi.exercise.reactive.schedulers.Scheduler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class Observable<T> {
    private static final AtomicInteger OBSERVABLE_COUNTER = new AtomicInteger(0);
    private static final Set<Integer> OBSERVABLE_SET = new CopyOnWriteArraySet<>();

    private final int ID = OBSERVABLE_COUNTER.incrementAndGet();
    private final Consumer<Observer<T>> source;

    private Disposable subscribe(Observer<T> observer) {
        final var id = OBSERVABLE_COUNTER.incrementAndGet();
        OBSERVABLE_SET.add(id);
        final var proxyObserver = new AbstractObserver<T>(id) {
            @Override
            public Observer<T> onNext(T item) {
                if (OBSERVABLE_SET.contains(getId())) {
                    observer.onNext(item);
                }

                return this;
            }

            @Override
            public Observer<T> onError(Throwable t) {
                if (OBSERVABLE_SET.contains(getId())) {
                    observer.onError(t);
                }

                return this;
            }

            @Override
            public void onComplete() {
                if (OBSERVABLE_SET.contains(getId())) {
                    observer.onComplete();
                }
            }
        };

        try {
            source.accept(proxyObserver);
        } catch (Exception e) {
            if (OBSERVABLE_SET.contains(proxyObserver.getId())) {
                observer.onError(e);
            }
        }

        return () -> {
            OBSERVABLE_SET.remove(id);
        };
    }

    public Disposable subscribe(Consumer<T> onNext, Consumer<Throwable> onError, Runnable onComplete) {
        final var observer = new Observer<T>() {
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
        };

        return subscribe(observer);
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

    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new Observable<>(observer -> subscribe(
                new Observer<>() {
                    @Override
                    public Observer<T> onNext(T item) {
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
                new Observer<>() {
                    @Override
                    public Observer<T> onNext(T item) {
                        try {
                            if (predicate.test(item)) {
                                observer.onNext(item);
                            }
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

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new Observable<>(observer -> {
            if (observer instanceof AbstractObserver) {
                OBSERVABLE_SET.remove(((AbstractObserver<?>) observer).getId());
            }

            scheduler.execute(() -> subscribe(observer));
        });
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new Observable<>(observer -> {
            if (observer instanceof AbstractObserver) {
                OBSERVABLE_SET.remove(((AbstractObserver<?>) observer).getId());
            }

            subscribe(
                item -> scheduler.execute(() -> observer.onNext(item)),
                error -> scheduler.execute(() -> observer.onError(error)),
                () -> scheduler.execute(observer::onComplete)
            );

        });
    }
}
