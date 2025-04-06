package mephi.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Задание 3.10.2
 * Условие
 * Представим ситуацию, где в системе есть несколько пользователей, у каждого из которых имеется некоторый баланс
 * (количество условных денежных единиц). Пользователи идентифицируются целочисленным id, начиная с 0: {0,1, 2,…, n−1}.
 * Также имеется очередь транзакций, где каждая транзакция — это перевод средств от одного пользователя к другому в формате.
 * Нужно с помощью Executor Framework обработать очередь транзакций и вывести итоговые данные о счетах каждого пользователя.
 *
 * Формат входных данных:
 *
 * Подается целое число n — количество пользователей.
 * Дальше идет список из n чисел, каждое соответствует начальному балансу i-того пользователя.
 *
 * После этого на отдельной строке подается число m — количество транзакций.
 * Далее подаются транзакции каждая на отдельной строке в формате: fromId — moneyCount — toId. fromId — с чьего счета
 * списывать, toId — на чей счет добавлять, moneyCount — кол-во денежных единиц.
 */
public class ConcurentTransactions {

    private static final Map<Integer, Integer> mapOfBalance = new HashMap<>();

    static class Transaction {
        final int fromId;
        final int toId;
        final int amount;

        Transaction(int fromId, int toId, int amount) {
            this.fromId = fromId;
            this.toId = toId;
            this.amount = amount;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Количество пользователей: ");
        int userCount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < userCount; i++) {
            System.out.printf("Баланс пользователя %d: ", i);
            mapOfBalance.put(i, Integer.parseInt(scanner.nextLine()));
        }

        System.out.print("Количество транзакций: ");
        int transCount = Integer.parseInt(scanner.nextLine());

        List<String> inputs = new ArrayList<>(transCount);
        for (int i = 0; i < transCount; i++) {
            System.out.printf("Транзакция №%d: ", i);
            String input = scanner.nextLine();
            inputs.add(input);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(userCount);
        ReentrantLock reentrantLock = new ReentrantLock();
        List<Future<?>> tasks = new ArrayList<>(transCount);
        for (String input : inputs) {
            String[] operands = input.split("-");
            Transaction transaction = new Transaction(
                    Integer.parseInt(operands[0].trim()),
                    Integer.parseInt(operands[2].trim()),
                    Integer.parseInt(operands[1].trim())
            );
            tasks.add(executorService.submit(new Task(transaction, reentrantLock)));
        }
        executorService.shutdown();

        while (tasks.stream().noneMatch(Future::isDone)) {
            Thread.sleep(10);
        }

        System.out.println();
        mapOfBalance.forEach((key, value) -> {
            System.out.printf("User %d final balance: %d", key, value);
            System.out.println();
        });

    }

    static class Task implements Runnable {

        private final Transaction transaction;
        private final ReentrantLock reentrantLock;

        Task(Transaction transaction, ReentrantLock reentrantLock) {
            this.transaction = transaction;
            this.reentrantLock = reentrantLock;
        }

        @Override
        public void run() {
            try {
                while (!reentrantLock.tryLock(1, TimeUnit.MILLISECONDS)) {
                    Thread.sleep(10);
                }
                if (mapOfBalance.containsKey(transaction.fromId)
                        && mapOfBalance.containsKey(transaction.toId)) {
                    mapOfBalance.put(transaction.fromId, mapOfBalance.get(transaction.fromId) - transaction.amount);
                    mapOfBalance.put(transaction.toId, mapOfBalance.get(transaction.toId) + transaction.amount);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
