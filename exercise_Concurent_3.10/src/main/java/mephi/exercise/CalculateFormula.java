package mephi.exercise;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Задание 3.10.1
 * Условие
 * Вам необходимо реализовать асинхронное вычисление формулы с помощью CompletableFuture и его функционала по комбинированию и асинхронному выполнению задач:
 *
 * double result = (a ^ 2 + b ^ 2) * log(c) / sqrt(d)
 * Эта формула состоит из нескольких частей, каждая из которых должна быть рассчитана отдельно:
 *
 * * Сумма квадратов: a ^ 2 + b ^ 2
 * * Натуральный логарифм: log⁡(c)
 * * Квадратный корень: sqrt(d)
 * Для каждой из этих операций нужно написать метод, который будет выполнять соответствующие вычисления с небольшой
 * задержкой для симуляции реальных условий (например, обращения к базе данных или внешнему API).
 * Все этапы должны быть выполнены асинхронно.
 */
public class CalculateFormula {

    // double result = (a ^ 2 + b ^ 2) * log(c) / sqrt(d)
    public static void main(String[] args) {
        final double a = Double.parseDouble(args[0]);
        final double b = Double.parseDouble(args[1]);
        final double c = Double.parseDouble(args[2]);
        final double d = Double.parseDouble(args[3]);

        CompletableFuture<Double> future1 = CompletableFuture.supplyAsync(() -> Math.pow(a, 2) + Math.pow(b, 2),
                CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS));
        CompletableFuture<Double> future2 = CompletableFuture.supplyAsync(() -> Math.log(c),
                CompletableFuture.delayedExecutor(15, TimeUnit.SECONDS));
        CompletableFuture<Double> future3 = CompletableFuture.supplyAsync(() -> Math.sqrt(d),
                CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS));

        CompletableFuture.allOf(future1, future2, future3).join();

        final double result1 = future1.join();
        System.out.println("Calculating sum of squares: " + result1);
        final double result2 = future2.join();
        System.out.println("Calculating sqrt(d): " + result2);
        final double result3 = future3.join();
        System.out.println("Calculating log(c): " + result3);

        double result = result1 * result2 / result3;
        System.out.println("Final result of the formula: " + result);
    }
}