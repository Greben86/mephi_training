package mephi.exercise;

import java.util.Scanner;
import java.util.Stack;

public class SimpleCalculator {
    public static void main(String[] args) {
        System.out.println("Для вычисления введи число, затем оператор, затем второе число");
        System.out.println("Доступные операторы +, -, *, /");
        System.out.println("Результат выполнения выражения сохраняется в буфере и участвует в следующем выражении как первое число");
        System.out.println("Чтобы очистить результат выполнения предыдущего выражения введи 'c'");
        System.out.println("Чтобы завершить выполнение введи 's'");
        System.out.println();
        try (Scanner scanner = new Scanner(System.in)) {
            calculator(scanner);
        }
    }

    public static void calculator(Scanner scanner) {
        Double operandA;
        Double operandB;
        String operation = "";
        Stack<Double> operands = new Stack<>();
        while (true) {
            try {
                if (operands.isEmpty()) {
                    System.out.print("Новое выражение: ");
                    operands.push(Double.parseDouble(scanner.next()));
                    continue;
                }

                if (operation.isEmpty()) {
                    System.out.print(operands.peek());
                    operation = scanner.next();
                    if (operation.length() > 1) {
                        throw new IllegalArgumentException("Неизвестный оператор " + operation);
                    }
                    switch (operation.charAt(0)) {
                        case 's':
                            return;
                        case 'c':
                            operation = "";
                            operands.clear();
                        default:
                            continue;
                    }
                }

                if (operands.size() == 1) {
                    System.out.print(operands.peek() + operation);
                    operands.push(Double.parseDouble(scanner.next()));
                    continue;
                }

                // На вершине стека лежит всегда второй операнд
                operandB = operands.pop();
                operandA = operands.pop();
                operands.push(switch (operation.charAt(0)) {
                    case '+' -> operandA + operandB;
                    case '-' -> operandA - operandB;
                    case '*' -> operandA * operandB;
                    case '/' -> operandA / operandB;
                    default -> throw new IllegalStateException("Неизвестная операция: " + operation);
                });
                System.out.println(operandA + operation + operandB + " = " + operands.peek());
                operation = "";
            } catch (Exception ex) {
                System.out.println(ex.getClass().getCanonicalName() + ": " + ex.getMessage());
                operation = "";
                operands.clear();
            }
        }
    }
}