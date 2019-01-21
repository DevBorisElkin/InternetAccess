package ru.startandroid.hw3_internetaccess;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

public class Notifier {

    Activity activity;
    Context context;
    private String city;
    private int temperature;
    private String weatherState;
    String icon;

    public Notifier(Activity activity, Context context, String city, int temperature, String weatherState, String icon) {
        this.city = city;
        this.temperature = temperature;
        this.weatherState = weatherState;
        this.context = context;
        this.activity = activity;
        this.icon = icon;
    }

    public void weatherNotify() {
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel("123", "awdda", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "123");
        mBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.clear_night)
                .setTicker("Hearty365")
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setContentTitle("WeatherApp")
                .setContentText(city + ". " + temperature + ", " + weatherState)
                .setContentInfo("Info");
        notificationManager.notify((int) (System.currentTimeMillis() / 1000), mBuilder.build());
    }
}