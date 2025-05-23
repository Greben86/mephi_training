package mephi.exercise;

import lombok.extern.slf4j.Slf4j;
import mephi.exercise.reactive.Observable;
import mephi.exercise.reactive.schedulers.ComputationScheduler;
import mephi.exercise.reactive.schedulers.IOThreadScheduler;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Using flatMap");
        new Observable<Integer>(observer -> {
                    try {
                        observer.onNext(1)
                                .onNext(2)
                                .onNext(3)
                                .onComplete();
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                })
                .flatMap(number -> new Observable<Integer>(observer -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                        observer.onNext(number * 10)
                                .onNext(number * 20)
                                .onComplete();
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                }))
                .subscribe(
                        item -> log.info("FlatMap result: {}", item),
                        error -> log.info("Error: {}", error.getMessage()),
                        () -> log.info("FlatMap done!")
                );

        log.info("Error handling");
        new Observable<Integer>(observer -> {
                    try {
                        observer.onNext(1)
                                .onNext(2);
                        throw new RuntimeException("Fake error");
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                })
                .subscribe(
                        item -> log.info("Task: {}", item),
                        error -> log.info("Error: {}", error.getMessage()),
                        () -> log.info("Done")
                );

        log.info("Disposable usage");
        final var disposableStream = new Observable<Integer>(observer -> {
            int i = 0;
            for (;;) {
                observer.onNext(i++);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        })
        .subscribeOn(new IOThreadScheduler())
        .observeOn(new ComputationScheduler(Runtime.getRuntime().availableProcessors()))
        .subscribe(
                item -> log.info("Task: {}", item),
                error -> log.info("Error: {}", error.getMessage()),
                () -> log.info("Complete")
        );

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            disposableStream.dispose();
            log.info("Stream disposed");
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}