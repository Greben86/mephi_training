package mephi.exercise;

import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        var executor = CustomExecutorImpl.newCustomThreadPool(new CustomThreadFactory());
        for (int i = 0; i < 50; i++) {
            executor.execute(Main::exampleTask);
        }
        for (int i = 0; i < 50; i++) {
            Future<String> future = executor.submit(Main::exampleFutureTask);
        }
        executor.shutdown();
    }

    private static void exampleTask() {
        try {
            Thread.sleep(100);
            System.out.println("123");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String exampleFutureTask() {
        try {
            Thread.sleep(100);
            System.out.println("321");
            return "success";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}