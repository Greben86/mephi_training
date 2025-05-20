package mephi.exercise.pool;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Worker implements Runnable {

    private boolean running = true;
    private final int id;
    private final InterruptedSupplier<Runnable> supplierTask;
    private final InterruptedSupplier<Status> supplierCheck;
    @Getter
    private Status status = Status.WAITING;

    @Override
    public void run() {
        Runnable task;
        try {
            do {
                while (running && (task = supplierTask.get(id)) != null) {
                    status = Status.RUNNING;
                    task.run();
                }
                status = Status.WAITING;
            } while ((status = supplierCheck.get(id)) != Status.STOPPED);
            log.info("Worker was stopped");
        } catch (InterruptedException e) {
            status = Status.STOPPED;
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
