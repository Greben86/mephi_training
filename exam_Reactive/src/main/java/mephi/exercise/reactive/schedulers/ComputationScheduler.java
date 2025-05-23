package mephi.exercise.reactive.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputationScheduler implements Scheduler {
    private final ExecutorService executor;

    public ComputationScheduler(final int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
