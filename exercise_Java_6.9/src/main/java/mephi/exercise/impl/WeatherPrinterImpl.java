package mephi.exercise.impl;

import mephi.exercise.WeatherPrinter;
import mephi.exercise.WeatherRecord;

import java.io.PrintStream;

/**
 * Принтер, выводит в консоль результаты парсинга погоды
 */
public class WeatherPrinterImpl implements WeatherPrinter {

    private final PrintStream out;

    public WeatherPrinterImpl(PrintStream out) {
        this.out = out;
    }

    @Override
    public void print(WeatherRecord weatherRecord) {
        out.println("Ответ сервиса: \n" + weatherRecord.allWeather());
        out.println();
        out.printf("Температура сегодня: %.2f%n", weatherRecord.tempToday());
        out.printf("Средняя температура за период: %.2f%n", weatherRecord.tempAvg());
    }
}
