package mephi.exercise;

import lombok.extern.slf4j.Slf4j;
import mephi.exercise.pool.CustomExecutorImpl;
import mephi.exercise.pool.CustomThreadFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadPoolDemo {
    private static final AtomicInteger completedTasks = new AtomicInteger(0);
    private static final AtomicInteger rejectedTasks = new AtomicInteger(0);

    public static void main(String[] args) {
        // Create thread pool with parameters
        final var pool = new CustomExecutorImpl(
                2,  // corePoolSize
                4,  // maxPoolSize
                5L,  // keepAliveTime
                TimeUnit.SECONDS,
                5,  // queueSize
                1,   // minSpareThreads
                new CustomThreadFactory()
        );

        log.info("Starting thread pool demonstration...");

        // Scenario 1: Normal operation with moderate load
        log.info("\nScenario 1: Normal operation with moderate load");
        submitTasks(pool, 10, 1000);
        waitForTasks(15);

        // Scenario 2: High load with potential rejections
        log.info("\nScenario 2: High load with potential rejections");
        submitTasks(pool, 20, 500);
        waitForTasks(15);

        // Scenario 3: Burst of tasks
        log.info("\nScenario 3: Burst of tasks");
        submitTasks(pool, 30, 200);
        waitForTasks(15);

        // Scenario 4: Long-running tasks
        log.info("\nScenario 4: Long-running tasks");
        submitTasks(pool, 5, 5000);
        waitForTasks(10);

        // Shutdown the pool
        log.info("\nInitiating pool shutdown...");
        pool.shutdown();

        // Print final statistics
        log.info("\nFinal Statistics:");
        log.info("Total completed tasks: {}", completedTasks.get());
        log.info("Total rejected tasks: {}", rejectedTasks.get());
    }

    private static void submitTasks(CustomExecutorImpl pool, int count, int sleepTime) {
        for (int i = 0; i < count; i++) {
            final int taskId = i;
            try {
                pool.execute(() -> {
                    log.info("Task {} started", taskId);
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    log.info("Task {} completed", taskId);
                });
            } catch (Exception e) {
                rejectedTasks.incrementAndGet();
                log.warn("Task {} was rejected", taskId);
            }
        }
    }

    private static void waitForTasks(int seconds) {
        try {
            log.info("Waiting for {} seconds...", seconds);
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
