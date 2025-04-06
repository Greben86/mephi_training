package mephi.exercise.service;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

/**
 * Класс умеет печатать простые сообщения и ошибки
 */
@RequiredArgsConstructor
public class Printer {

    private final PrintStream out;
    private final PrintStream err;

    /**
     * Метод печатает пустое сообщение с переводом строки
     */
    public void info() {
        out.println();
    }

    /**
     * Метод печатает простое сообщение с переводом строки
     *
     * @param message сообщение
     */
    public void info(String message) {
        out.println(message);
    }

    /**
     * Метод печатает простое сообщение без перевода строки
     *
     * @param message сообщение
     */
    public void print(String message) {
        out.print(message);
    }

    /**
     * Метод печатает ошибку
     *
     * @param message текст ошибки
     */
    public void error(String message) {
        err.println(message);
    }
}
