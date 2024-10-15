package mephi.exercise.impl;

import mephi.exercise.PropertyLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Загрузчик настроек из файла и переменных среды
 */
public class PropertyLoaderImpl implements PropertyLoader {

    /**
     * Так как Properties, Map, Collection пока не проходили, я решил сделать свой велосипед с построчным чтением файла и дальнейшим парсингом
     */
    @Override
    public String getProperty(String param) throws IOException {
        try (InputStream inputStream = new FileInputStream("exercise_Java_6.9/src/main/resources/config.properties");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] keyValue = line.split("=");
                String key = keyValue[0];
                String value = keyValue[1];
                if (param.equals(key)) {
                    return parseProperty(value);
                }
            }
        }

        return "";
    }

    /**
     * Если попался параметр в скобках ${param_name}, ищем в переменных окружения param_name и подставляем значение
     * Некоторые параметры не нужно держать в файле настроек, например ключ от API
     */
    private String parseProperty(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            String nameEnvVar = value.substring(value.indexOf('{') + 1, value.indexOf('}'));
            return System.getenv(nameEnvVar) != null ? System.getenv(nameEnvVar) : value;
        }

        return value;
    }
}
