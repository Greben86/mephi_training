package mephi.exercise.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import mephi.exercise.WeatherParser;
import mephi.exercise.WeatherRecord;

/**
 * Парсер, парсит то что пришло из сервиса погоды
 */
public class WeatherParserImpl implements WeatherParser {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public WeatherRecord parse(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        float tempToday = readTempToday(jsonObject);
        float tempAvg = readAvgTemp(jsonObject);
        String prettyJson = makePrettyJson(jsonObject);
        return new WeatherRecord(prettyJson, tempToday, tempAvg);
    }

    /**
     * Температура сегодня, лежит в fact -> temp
     */
    public static float readTempToday(JsonObject root) {
        JsonElement result = root;
        for (String key : new String[]{"fact", "temp"}) {

            key = key.trim();
            if (key.isEmpty()) {
                continue;
            }

            if (result == null){
                result = JsonNull.INSTANCE;
                break;
            }

            if (result.isJsonObject()) {
                result = ((JsonObject) result).get(key);
            } else {
                break;
            }
        }

        return result != null && !result.isJsonNull() ? result.getAsFloat() : 0.F;
    }

    /**
     * Средняя температура, берется как сумма всех элементов (средняя температура за часть суток)
     * forecasts -> parts -> [morning, day, evening, night] -> temp_avg
     * И делится на общее количество этих элементов
     */
    public static float readAvgTemp(JsonObject root) {
        float allAvg = 0.F;
        int count = 0;

        if (!root.has("forecasts")) {
            return allAvg;
        }

        JsonArray forecasts = (JsonArray) root.get("forecasts");
        for (JsonElement forecast : forecasts) {
            if (!((JsonObject) forecast).has("parts")) {
                continue;
            }

            JsonObject parts = (JsonObject) ((JsonObject) forecast).get("parts");
            for (String key : new String[]{"morning", "day", "evening", "night"}) {
                if (!parts.has(key)) {
                    continue;
                }

                JsonObject part = (JsonObject) parts.get(key);
                if (part.has("temp_avg")) {
                    JsonElement temp_avg = part.get("temp_avg");
                    allAvg += temp_avg != null ? temp_avg.getAsFloat() : 0.F;
                    count++;
                }
            }
        }

        return count > 0 ? allAvg / count : 0.F;
    }

    /**
     * Для того чтобы json нормально отображался, его нужно форматировать - прочитать в JsonObject, а потом обратно в JSON-строку
     */
    private String makePrettyJson(JsonObject root) {
        return gson.toJson(root);
    }
}
