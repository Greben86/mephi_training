package mephi.exercise;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class AdvancedCalculator {

    private static final List<Character> ALLOWED_OPERATIONS = Arrays.asList('+', '-', '*', '/');

    public static void main(String[] args) {
        System.out.println("Введи выражение для вычисления в формате a+b+c...");
        System.out.println("Доступные выражения +, -, *, /, скобки не поддерживаются");
        System.out.println("Результат выполнения выражения сохраняется в буфере и участвует в следующем выражении");
        System.out.println("Чтобы очистить результат выполнения предыдущего выражения введи 'c'");
        System.out.println("Чтобы завершить выполнение введи 's'");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        StringBuilder expression = new StringBuilder();
        String value = "";
        while (true) {
            System.out.print(value.isEmpty() ? "Новое выражение: " : value);
            String input = scanner.next();

            if (input.isEmpty()) {
                continue;
            }

            if ("s".equalsIgnoreCase(input)) {
                break;
            }

            if ("c".equalsIgnoreCase(input)) {
                System.out.println(expression + " = " + value);
                expression.setLength(0);
                value = "";
                continue;
            }

            value = String.valueOf(evaluate(value + input));
            expression.append(input);
            System.out.println();
        }

        if (!expression.isEmpty()) {
            System.out.println(expression + " = " + value);
        }
    }

    private static double evaluate(String expression) {
        // Отделяем операнды от операций и сохраняем в коллекции
        StringBuilder buffer = new StringBuilder();
        List<Double> operandList = new ArrayList<>();
        List<Character> operationList = new ArrayList<>();
        for (char current : expression.toCharArray()) {
            if (Character.isDigit(current) || '.' == current || ('-' == current && buffer.isEmpty())) {
                buffer.append(current);
                continue;
            }
            if (!buffer.isEmpty()) {
                operandList.add(Double.parseDouble(buffer.toString()));
                buffer.setLength(0);
            }
            if (ALLOWED_OPERATIONS.contains(current)) {
                operationList.add(current);
            }
        }
        if (!buffer.isEmpty()) {
            operandList.add(Double.parseDouble(buffer.toString()));
        }

        // Записываем коллекцию в стек в обратном порядке, для того чтобы на вершине стека оказался самый первый элемент
        Deque<Character> operationStack = new ArrayDeque<>(operationList.size());
        for (int index = operationList.size()-1; index >=0; index--) {
            operationStack.push(operationList.get(index));
        }

        Deque<Double> operandStack = new ArrayDeque<>();
        for (int index = operandList.size()-1; index >=0; index--) {
            operandStack.push(operandList.get(index));
        }

        Double x;
        Double y;
        // В цикле берем операции из стека
        while (!operationStack.isEmpty()) {
            // Для каждой операции берем два операнда из стека, затем выполняем вычисление и кладем на вершину стека
            x = operandStack.pop();
            y = operandStack.pop();
            Character operation = operationStack.pop();
            operandStack.push(switch (operation) {
                case '+' -> x + y;
                case '-' -> x - y;
                case '*' -> x * y;
                case '/' -> x / y;
                default -> throw new IllegalStateException("Неизвестная операция: " + operation); // Хоть тут и не может быть неизвестных операций
            });
        }

        // Тут в стеке операндов должно остаться только одно значение
        if (operandStack.size() != 1) {
            throw new IllegalStateException("Некорректное выражение " + expression);
        }

        return operandStack.pop();
    }
}