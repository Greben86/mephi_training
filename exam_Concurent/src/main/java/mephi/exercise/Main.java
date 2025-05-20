package mephi.exercise;

import lombok.extern.slf4j.Slf4j;
import mephi.exercise.pool.CustomExecutorImpl;
import mephi.exercise.pool.CustomThreadFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {
        final var pool = new CustomExecutorImpl(
                2,  // corePoolSize
                10,  // maxPoolSize
                1L,  // keepAliveTime
                TimeUnit.SECONDS,
                10,  // queueSize
                2,   // minSpareThreads
                new CustomThreadFactory()
        );
        for (int i = 0; i < 50; i++) {
            pool.execute(Main::exampleTask);
        }
        for (int i = 0; i < 50; i++) {
            Future<String> future = pool.submit(Main::exampleFutureTask);
        }
        TimeUnit.SECONDS.sleep(10);
        pool.shutdown();
    }

    private static void exampleTask() {
        try {
            TimeUnit.MILLISECONDS.sleep(120);
            log.info("123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String exampleFutureTask() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            log.info("321");
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}