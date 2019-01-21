package ru.startandroid.hw3_internetaccess;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataLoader {

    private static final String appId = "b3a09586a1b67e9477607db6dc59e06a";
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;

    //Единственный метод класса, который делает запрос на сервер и получает от него данные
    //Возвращает объект JSON или null
    public static JSONObject getJSONData(String city) {
        try {
            //Используем API (Application programming interface) openweathermap
            String input=String.format(OPEN_WEATHER_MAP_API, city, appId);
            URL url = new URL(input);

//            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city, appId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append(NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            // API openweathermap
            if (jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            }
            return jsonObject;
        } catch (Exception e) {
            return null; //FIXME Обработка ошибки
        }
    }
}
