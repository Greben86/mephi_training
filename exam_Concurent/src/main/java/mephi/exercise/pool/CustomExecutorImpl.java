package mephi.exercise.pool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class CustomExecutorImpl implements CustomExecutor {

    private final int corePoolSize;
    private final int maxPoolSize;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final int queueSize;
    private final int minSpareThreads;
    private final ThreadFactory threadFactory;
    private final RejectedExecutionHandler rejectionHandler;

    private final ReentrantLock mainLock = new ReentrantLock();
    private final AtomicInteger activeThreads = new AtomicInteger(0);
    private final AtomicInteger currentPoolSize = new AtomicInteger(0);
    private final AtomicBoolean shutdownFlag = new AtomicBoolean(false);
    private Map<Integer, Worker> workers;
    private Map<Integer, BlockingQueue<Runnable>> queues;
    private RoundRobinLoadBalancer<Worker> balancer;

    public CustomExecutorImpl(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, int queueSize,
                              int minSpareThreads, ThreadFactory threadFactory) {
        if (corePoolSize < 0 || maxPoolSize <= 0 || maxPoolSize < corePoolSize ||
                keepAliveTime < 0 || queueSize <= 0 || minSpareThreads < 0) {
            throw new IllegalArgumentException("Invalid thread pool parameters");
        }

        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.queueSize = queueSize;
        this.minSpareThreads = minSpareThreads;
        this.threadFactory = threadFactory;
        this.rejectionHandler = new CustomRejectionHandler();

        init();
    }

    private void init() {
        workers = new HashMap<>(maxPoolSize);
        queues = new HashMap<>(maxPoolSize);
        balancer = new RoundRobinLoadBalancer<>(workers);
        for (int i = 0; i < corePoolSize; i++) {
            startNewWork();
        }
    }

    private void startNewWork() {
        int id = currentPoolSize.incrementAndGet();
        Worker worker = new Worker(id, this::nextTask);
        workers.put(id, worker);
        queues.put(id, new ArrayBlockingQueue<>(queueSize));
        threadFactory.newThread(worker).start();
    }

    private Runnable nextTask(final int id) throws InterruptedException {
        BlockingQueue<Runnable> queue;
        if ((queue = queues.get(id)) != null) {
            return queue.poll(keepAliveTime, timeUnit);
        }
        return null;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException("Task cannot be null");
        }
        if (shutdownFlag.get()) {
            return;
        }

        mainLock.lock();
        try {
            final int activeCount = activeThreads.get();
            final int currentSize = currentPoolSize.get();

            if (activeCount >= currentSize && currentSize < maxPoolSize) {
                startNewWork();
                log.info("Created new worker thread. Current pool size: {}", currentPoolSize.get());
            }

            final int id = balancer.getNextThreadId();
            final var targetQueue = queues.get(id);
            final var task = new TaskWrapper(command);
            if (!targetQueue.offer(task)) {
                rejectionHandler.rejectedExecution(task, null);
            } else {
                log.warn("Task submitted to queue {}", id);
            }
        } finally {
            mainLock.unlock();
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        if (callable == null) {
            throw new NullPointerException("Task cannot be null");
        }
        if (shutdownFlag.get()) {
            return null;
        }

        final var result = new FutureTask<>(callable);
        execute(result);
        return result;
    }

    @Override
    public void shutdown() {
        shutdownFlag.getAndSet(true);
    }

    @Override
    public void shutdownNow() {
        mainLock.lock();
        try {
            shutdownFlag.set(true);
            workers.forEach((id, worker) -> worker.stop());
        } finally {
            mainLock.unlock();
        }
    }

    private void manageThreadPool() {
        Long currentTime = System.currentTimeMillis();
        workers.entrySet().removeIf(worker ->
                currentTime - worker.getValue().getLastTaskTime() > keepAliveTime);

    }

    @RequiredArgsConstructor
    public static class RoundRobinLoadBalancer<T> {
        private final Map<Integer, T> threads;
        private int currentIndex = 0;

        public int getNextThreadId() {
            int index = 0;
            for (Map.Entry<Integer, T> entry : threads.entrySet()) {
                if (index == currentIndex) {
                    currentIndex = (currentIndex + 1) % threads.size();
                    return entry.getKey();
                }
                index++;
            }

            throw new IllegalStateException();
        }
    }

    @RequiredArgsConstructor
    private class TaskWrapper implements Runnable {

        private final Runnable task;

        @Override
        public void run() {
            activeThreads.incrementAndGet();
            try {
                task.run();
            } finally {
                activeThreads.decrementAndGet();
            }
        }
    }

    private static class CustomRejectionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.warn("Task rejected: {}", r.toString());
            throw new RejectedExecutionException("Task rejected: " + r);
        }
    }
}
