package mephi.exercise.pool;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Worker implements Runnable {

    private boolean running = true;
    private final int id;
    private final InterruptedSupplier<Runnable> supplier;
    @Getter
    private Status status = Status.WAITING;
    @Getter
    private Long lastTaskTime = System.currentTimeMillis();

    @Override
    public void run() {
        Runnable task;
        try {
            do {
                while (running && (task = supplier.get(id)) != null) {
                    lastTaskTime = System.currentTimeMillis();
                    status = Status.RUNNING;
                    task.run();
                }
                status = Status.WAITING;
            } while (running);
            status = Status.STOPPED;
            log.info("Worker was stopped");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Передача сигнала об остановке
     */
    public void stop() {
        running = false;
    }
}
