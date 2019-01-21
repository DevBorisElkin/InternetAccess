package ru.startandroid.hw3_internetaccess;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherWidgetConfigureActivity WeatherWidgetConfigureActivity}
 */
public class WeatherWidget extends AppWidgetProvider {
    private static final String TAG = "StartedService";
    boolean b;
    private static final Handler handler = new Handler();
    public static String City;
    public static String TemperatureCelcius;
    public static String TemperatureFarenheits;
    public static String weatherState;
    public static String CURRENT_CITY;
    public static String TEMP_IN_CURRENT_CITY;
    public Drawable drawable;
    public static String Icon;
    public int color;
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String ITEM_ON_CLICK_ACTION = "android.appwidget.action.ITEM_ON_CLICK";
    public static final String NOTE_TEXT = "note_text";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WeatherWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {

        CharSequence cityCharSequence=CURRENT_CITY;
        CharSequence temperatureCelcius=TEMP_IN_CURRENT_CITY;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.city_text, cityCharSequence);
        views.setTextViewText(R.id.temperature_text, temperatureCelcius);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        String action = intent.getAction();

        if (action != null) {
            if (action.equalsIgnoreCase(UPDATE_WIDGET_ACTION)) {
                int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
                mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.city_text);
                mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.temperature_text);
            }
            if (action.equalsIgnoreCase(ITEM_ON_CLICK_ACTION)) {
                String itemText = intent.getStringExtra(NOTE_TEXT);
                if (!itemText.equalsIgnoreCase("")) {
                    Toast.makeText(context, itemText, Toast.LENGTH_SHORT).show();
                }
            }

        }
        super.onReceive(context, intent);
    }
}

