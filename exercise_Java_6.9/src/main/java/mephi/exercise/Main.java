package mephi.exercise;

import mephi.exercise.impl.PropertyLoaderImpl;
import mephi.exercise.impl.WeatherParserImpl;
import mephi.exercise.impl.WeatherPrinterImpl;
import mephi.exercise.impl.WeatherRestClientImpl;

import java.util.Scanner;

/**
 * Главный класс
 */
public class Main {

    private final PropertyLoader propertyLoader;
    private final WeatherRestClient weatherRestClient;
    private final WeatherParser weatherParser;
    private final WeatherPrinter weatherPrinter;

    public Main() {
        this.propertyLoader = new PropertyLoaderImpl();
        this.weatherRestClient = new WeatherRestClientImpl(propertyLoader);
        this.weatherParser = new WeatherParserImpl();
        this.weatherPrinter = new WeatherPrinterImpl(System.out);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.execute();
    }

    void execute() {
        System.out.println("Для получения прогноза погоды необходимо задать период");
        System.out.println("Период - количество дней для прогноза");
        System.out.println("Для тарифа «Тестовый» максимально допустимое значение — 7");
        System.out.println("Если будет указано значение 0, то параметр не будет использоваться");
        System.out.println();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Количество дней прогноза: ");
            String limit = scanner.next();
            System.out.println();
            String jsonWeather = weatherRestClient.getWeather(limit);
            WeatherRecord weatherRecord = weatherParser.parse(jsonWeather);
            weatherPrinter.print(weatherRecord);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}