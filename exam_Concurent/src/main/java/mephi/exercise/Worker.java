package mephi.exercise;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Worker implements Runnable {

    private final Long id;
    private final InterruptedSupplier<Runnable> supplier;
    @Getter
    private Long lastTaskTime = System.currentTimeMillis();

    @Override
    public void run() {
        Runnable task;
        try {
            while ((task = supplier.get(id)) != null) {
                task.run();
                lastTaskTime = System.currentTimeMillis();
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
