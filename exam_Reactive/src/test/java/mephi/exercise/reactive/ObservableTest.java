package mephi.exercise.reactive;

import mephi.exercise.reactive.schedulers.IOThreadScheduler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class ObservableTest {
    @Test
    void testBasicEmission() {
        final var received = new ArrayList<>();
        final var completed = new AtomicBoolean(false);

        new Observable<Integer>(observer -> {
            observer.onNext(1)
                    .onNext(2)
                    .onNext(3)
                    .onComplete();
        })
        .subscribe(
            received::add,
            e -> Assertions.fail(e.getMessage()),
            () -> completed.set(true)
        );

        Assertions.assertEquals(3, received.size());
        Assertions.assertEquals(1, received.get(0));
        Assertions.assertEquals(2, received.get(1));
        Assertions.assertEquals(3, received.get(2));
        Assertions.assertTrue(completed.get());
    }

    @Test
    void testErrorHandling() {
        final var errorReceived = new AtomicBoolean(false);
        final var errorMessage = "Error";

        new Observable<Integer>(observer -> {
            observer.onNext(1);
            throw new RuntimeException(errorMessage);
        })
        .subscribe(
            item -> {},
            error -> {
                errorReceived.set(true);
                Assertions.assertEquals(errorMessage, error.getMessage());
            },
            () -> Assertions.fail("Should not complete")
        );

        Assertions.assertTrue(errorReceived.get());
    }

    @Test
    void testMapOperator() {
        final var received = new ArrayList<>();

        new Observable<Integer>(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onComplete();
        })
        .map(x -> x << 2)
            .subscribe(
                received::add,
                error -> Assertions.fail("Error"),
                () -> {}
            );

        Assertions.assertEquals(2, received.size());
        Assertions.assertEquals(4, received.get(0));
        Assertions.assertEquals(8, received.get(1));
    }

    @Test
    void testFilterOperator() {
        final var received = new ArrayList<>();

        new Observable<Integer>(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onComplete();
        })
        .filter(x -> x % 2 == 0)
                .subscribe(
                        received::add,
                        error -> Assertions.fail("Unexpected error"),
                        () -> {}
                );

        Assertions.assertEquals(1, received.size());
        Assertions.assertEquals(2, received.get(0));
    }

    @Test
    void testFlatMapOperator() {
        final var received = new ArrayList<>();

        new Observable<Integer>(observer -> {
            observer.onNext(1)
                    .onNext(2)
                    .onComplete();
        })
        .flatMap(x -> new Observable<Integer>(observer -> {
                observer.onNext(x * 10);
                observer.onNext(x * 20);
                observer.onComplete();
            }))
            .subscribe(
                    received::add,
                    error -> Assertions.fail("Unexpected error"),
                    () -> {}
            );

        Assertions.assertEquals(4, received.size());
        Assertions.assertEquals(10, received.get(0));
        Assertions.assertEquals(20, received.get(1));
        Assertions.assertEquals(20, received.get(2));
        Assertions.assertEquals(40, received.get(3));
    }

    @Test
    void testDisposable() {
        final var count = new AtomicInteger(0);

        Disposable subscription = new Observable<Integer>(observer -> {
            observer.onNext(count.incrementAndGet());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        })
        .subscribeOn(new IOThreadScheduler())
        .subscribe(
            item -> {},
            error -> Assertions.fail("Unexpected error"),
            () -> Assertions.fail("Should not complete")
        );

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            subscription.dispose();
            int finalCount = count.get();
            TimeUnit.MILLISECONDS.sleep(200);
            Assertions.assertEquals(finalCount, count.get(), "Count should not increase after disposal");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assertions.fail("Test interrupted");
        }
    }
}