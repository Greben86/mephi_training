package mephi.exercise;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class CustomExecutorImpl implements CustomExecutor {

    private final int corePoolSize;
    private final int maxPoolSize;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final int queueSize;
    private final int minSpareThreads;
    private final ThreadFactory threadFactory;

    private final AtomicLong threadCounter = new AtomicLong(0L);
    private final AtomicBoolean shutdownFlag = new AtomicBoolean(false);
    private final AtomicBoolean shutdownNowFlag = new AtomicBoolean(false);
    private Map<Long, Worker> workers;
    private Map<Long, Queue<Runnable>> queues;
    private RoundRobinLoadBalancer<Worker> balancer;

    public CustomExecutorImpl(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, int queueSize,
                              int minSpareThreads, ThreadFactory threadFactory) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.queueSize = queueSize;
        this.minSpareThreads = minSpareThreads;
        this.threadFactory = threadFactory;

        workers = new HashMap<>(maxPoolSize);
        queues = new HashMap<>(maxPoolSize);
        for (int i = 0; i < corePoolSize; i++) {
            startNewWork();
        }

        this.balancer = new RoundRobinLoadBalancer<>(workers);
    }

    private void startNewWork() {
        Long id = threadCounter.incrementAndGet();
        Worker worker = new Worker(id, this::nextTask);
        workers.put(id, worker);
        queues.put(id, new ArrayDeque<>(queueSize));
        threadFactory.newThread(worker).start();
    }

    private Runnable nextTask(Long id) throws InterruptedException {
        var queue = queues.get(id);
        for (;!(shutdownFlag.get() && queue.isEmpty()); Thread.sleep(100)) {
            if (shutdownNowFlag.get()) {
                throw new InterruptedException();
            }
            if (queue.peek() != null) {
                return queue.poll();
            }
        }
        return null;
    }

    @Override
    public void execute(Runnable command) {
        Long id = balancer.getNextThreadId();
        queues.get(id).add(command);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> result = new FutureTask<>(callable);
        Long id = balancer.getNextThreadId();
        queues.get(id).add(result);
        return result;
    }

    @Override
    public void shutdown() {
        shutdownFlag.getAndSet(true);
    }

    @Override
    public void shutdownNow() {
        shutdownNowFlag.getAndSet(true);
    }

    private void manageThreadPool() {
        Long currentTime = System.currentTimeMillis();
        workers.entrySet().removeIf(worker ->
                currentTime - worker.getValue().getLastTaskTime() > keepAliveTime);

    }

    public static CustomExecutor newCustomThreadPool(ThreadFactory threadFactory) {
        return new CustomExecutorImpl(10, 100,
                60L, TimeUnit.SECONDS, 10, 10, threadFactory);
    }

    public class RoundRobinLoadBalancer<T> {
        private Map<Long, T> threads;
        private int currentIndex;

        public RoundRobinLoadBalancer(Map<Long, T> threads) {
            this.threads = threads;
            this.currentIndex = 0;
        }

        public Long getNextThreadId() {
            int index = 0;
            for (Map.Entry<Long, T> entry : threads.entrySet()) {
                if (index == currentIndex) {
                    currentIndex = (currentIndex + 1) % threads.size();
                    return entry.getKey();
                }
                index++;
            }

            throw new IllegalStateException();
        }
    }
}
