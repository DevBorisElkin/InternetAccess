package ru.startandroid.hw3_internetaccess.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import org.json.JSONObject;

import ru.startandroid.hw3_internetaccess.MainActivity;
import ru.startandroid.hw3_internetaccess.R;
import ru.startandroid.hw3_internetaccess.Weather;
import ru.startandroid.hw3_internetaccess.WeatherDataLoader;


public class StartedService extends IntentService {
    private static final String TAG = "StartedService";
    boolean b;
    private final Handler handler = new Handler();
    public String City;
    public String TemperatureCelcius;
    public String TemperatureFarenheits;
    public String weatherState;
    public Drawable drawable;
    public String Icon;
    public int color;



    public StartedService() {
        super("StartedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent start");
        Bundle bundle = intent.getExtras();
        String s = "";
        if (bundle != null) {
            s = bundle.getString(MainActivity.DATA_NEED_FOR_TEST, "Alice Springs");
        } else s="Alice Springs";
//       updateWeatherData(s);
        sleep(1000);

        while (b) {
            Log.e(TAG, "1 time passed");
            updateWeatherData(s);
            sleep(1000);
            if(City==null){

            }else{
                notification();
                sleep(600000);
            }


        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void notification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel("123", "awdda", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        String text;
        if(TemperatureCelcius==null&&weatherState==null){
            text="Something went wrong";
        }else{
            text=City + ". " + TemperatureCelcius + "°C, " + weatherState;
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "123");
        mBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Hearty365")
                .setColor(color)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("MyWeather")
                .setContentText(text)
                .setContentInfo("Info");
        switch(Icon){
            case "01d":
                mBuilder.setSmallIcon(R.drawable.sunny);
                break;
            case "01n":
                mBuilder.setSmallIcon(R.drawable.clear_night);
                break;
            case "02d":
                mBuilder.setSmallIcon(R.drawable.cloudy_sunny);
                break;
            case "02n":
                mBuilder.setSmallIcon(R.drawable.moon_night_cloudy);
                break;
            case "03d":
                mBuilder.setSmallIcon(R.drawable.cloudy);
                break;
            case "03n": //g
                mBuilder.setSmallIcon(R.drawable.cloudy);
                break;
            case "04d":
                mBuilder.setSmallIcon(R.drawable.broken_clouds);
                break;
            case "04n":
                mBuilder.setSmallIcon(R.drawable.broken_clouds);
                break;
            case "09d":
                mBuilder.setSmallIcon(R.drawable.shower_rain);
                break;
            case "09n":
                mBuilder.setSmallIcon(R.drawable.shower_rain);
                break;
            case "10d":
                mBuilder.setSmallIcon(R.drawable.day_sun_rain);
                break;
            case "10n":
                mBuilder.setSmallIcon(R.drawable.nigh_moon_rain);
                break;
            case "11d":
                mBuilder.setSmallIcon(R.drawable.thunderstorm);
                break;
            case "11n":
                mBuilder.setSmallIcon(R.drawable.thunderstorm);
                break;
            case "13d":
                mBuilder.setSmallIcon(R.drawable.snow);
                break;
            case "13n":
                mBuilder.setSmallIcon(R.drawable.snow);
                break;
            case "50d":
                mBuilder.setSmallIcon(R.drawable.mist);
                break;
            case "50n":
                mBuilder.setSmallIcon(R.drawable.mist);
                break;
            default:
                mBuilder.setSmallIcon(R.drawable.error);
                break;
        }
        notificationManager.notify((int) (System.currentTimeMillis() / 1000), mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        b = true;
        Log.w(TAG, "onStartCommand " + this.hashCode());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        b = false;
        Log.w(TAG, "onDestroy " + this.hashCode());
    }
    private void updateWeatherData(final String city) {
        new Thread() {//Отдельный поток для получения новых данных в фоне
            public void run() {
                final JSONObject json = WeatherDataLoader.getJSONData(city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            TemperatureCelcius=null;
                            weatherState=null;
                        }
                    });
                } else {
                    final Weather weather = new Gson().fromJson(json.toString(), Weather.class);
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(weather);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(Weather weather) {
        City = weather.getCity();
        Icon = weather.getIcon();
        String temp = weather.getTemp();
        double temperature = Double.parseDouble(temp);
        int tempInt = (int) temperature;
        TemperatureCelcius = Integer.toString(tempInt);
        int converted = tempInt * 9 / 5 + 32;
        TemperatureFarenheits = Integer.toString(converted);
        switch (Icon) {
            case "01d":
                drawable = getResources().getDrawable(R.drawable.sunny);
                //не смог передать отсюда NotificationBuilder'у иконку, поэтому 2 раза повторяется switch.
                //Строчку с drawable оставил на будущее, если узнаю, как исправить, то верхний switch можно убрать и одной строчкой установить.
                color=R.color.colorDay;
                weatherState = "clear sky, day";
                break;
            case "01n":
                drawable = getResources().getDrawable(R.drawable.clear_night);
                color=R.color.colorNight;
                weatherState = "clear sky, night";
                break;
            case "02d":
                drawable = getResources().getDrawable(R.drawable.cloudy_sunny);
                color=R.color.colorDay;
                weatherState = "few clouds, day";
                break;
            case "02n":
                drawable = getResources().getDrawable(R.drawable.moon_night_cloudy);
                color=R.color.colorNight;
                weatherState = "few clouds, night";
                break;
            case "03d":
                drawable = getResources().getDrawable(R.drawable.cloudy);
                color=R.color.colorDay;
                weatherState = "scattered clouds, day";
                break;
            case "03n": //g
                drawable = getResources().getDrawable(R.drawable.cloudy);
                color=R.color.colorNight;
                weatherState = "scattered clouds, night";
                break;
            case "04d":
                drawable = getResources().getDrawable(R.drawable.broken_clouds);
                color=R.color.colorBrokenClouds;
                weatherState = "broken clouds, day";
                break;
            case "04n":
                drawable = getResources().getDrawable(R.drawable.broken_clouds);
                color=R.color.colorBrokenClouds;
                weatherState = "broken clouds, night";
                break;
            case "09d":
                drawable = getResources().getDrawable(R.drawable.shower_rain);
                color=R.color.colorDay;
                weatherState = "shower rain, day";
                break;
            case "09n":
                drawable = getResources().getDrawable(R.drawable.shower_rain);
                color=R.color.colorNight;
                weatherState = "shower rain, night";
                break;
            case "10d":
                drawable = getResources().getDrawable(R.drawable.day_sun_rain);
                color=R.color.colorDay;
                weatherState = "rain, day";
                break;
            case "10n":
                drawable = getResources().getDrawable(R.drawable.nigh_moon_rain);
                color=R.color.colorNight;
                weatherState = "rain, night";
                break;
            case "11d":
                drawable = getResources().getDrawable(R.drawable.thunderstorm);
                color=R.color.colorBrokenClouds;
                weatherState = "thunderstorm, day";
                break;
            case "11n":
                drawable = getResources().getDrawable(R.drawable.thunderstorm);
                color=R.color.colorBrokenClouds;
                weatherState = "thunderstorm, night";
                break;
            case "13d":
                drawable = getResources().getDrawable(R.drawable.snow);
                color=R.color.colorSnow;
                weatherState = "snow, day";
                break;
            case "13n":
                drawable = getResources().getDrawable(R.drawable.snow);
                color=R.color.colorSnow;
                weatherState = "snow, night";
                break;
            case "50d":
                drawable = getResources().getDrawable(R.drawable.mist);
                color=R.color.colorFog;
                weatherState = "mist, day";
                break;
            case "50n":
                drawable = getResources().getDrawable(R.drawable.mist);
                color=R.color.colorFog;
                weatherState = "mist, night";
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.error);
                color=Color.RED;
                weatherState = "error, night";
                break;
        }
    }
}