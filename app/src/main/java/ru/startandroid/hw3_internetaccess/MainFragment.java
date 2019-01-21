package ru.startandroid.hw3_internetaccess;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.startandroid.hw3_internetaccess.Database.City;
import ru.startandroid.hw3_internetaccess.Database.CityDataSource;

public class MainFragment extends Fragment {

    private final Handler handler = new Handler();
    @BindView(R.id.city_field)
    TextView cityTextView;
    @BindView(R.id.updated_field)
    TextView updatedTextView;
    @BindView(R.id.details_field)
    TextView detailsTextView;
    @BindView(R.id.current_temperature_field)
    TextView currentTemperatureTextView;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.layout)
    LinearLayout linearLayout;
    @BindView(R.id.fahrenheits)
    TextView textF;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton fb;
    List<City> elements;
    CityDataSource cityDataSource;
    String city;
    String icon;
    String tempInCelcius;
    String tempInFarenheits;
    String weatherState;
    public static String tempCodeF = "tempF";
    public static String tempCodeC = "tempC";
    public static String cityCode = "city";
    public static String weatherCode = "icon";



    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initDb();
        initUI(view);
        initFB();
        return view;
    }

    private void initFB() {
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra(tempCodeF, tempInFarenheits);
                intent.putExtra(tempCodeC, tempInCelcius);
                intent.putExtra(cityCode, city);
                intent.putExtra(weatherCode, weatherState);

                startActivity(intent);
            }
        });
    }

    private void initDb() {
        new Thread() {
            public void run() {
                cityDataSource = new CityDataSource(getContext());
                cityDataSource.open();
                elements = cityDataSource.getAllCities();
            }
        }.start();
    }

    private void initUI(View view) {
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String i = bundle.getString(MainActivity.KEY, "");
            changeCity(i);
        }
    }


    //Обновление/загрузка погодных данных
    private void updateWeatherData(final String city) {
        new Thread() {//Отдельный поток для получения новых данных в фоне
            public void run() {
                final JSONObject json = WeatherDataLoader.getJSONData(city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
//
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
        city = weather.getCity();
        addElement(city);
        icon = weather.getIcon();
        String temp = weather.getTemp();
        double temperature = Double.parseDouble(temp);
        int tempInt = (int) temperature;
        tempInCelcius = Integer.toString(tempInt);
        int converted = tempInt * 9 / 5 + 32;
        tempInFarenheits = Integer.toString(converted);
        textF.setText(tempInFarenheits + "°F");
        String pressure = weather.getPressure();
        String wind = weather.getWind();
        wind=wind.replaceAll("[{]","(");
        wind=wind.replaceAll("[}]",")");
        cityTextView.setText(city);
        updatedTextView.setText(wind);
        detailsTextView.setText(pressure);
        currentTemperatureTextView.setText(tempInCelcius + "°C");
        setWeatherIcon(weather.getIcon());



        WeatherWidget.CURRENT_CITY=city;
        WeatherWidget.TEMP_IN_CURRENT_CITY=tempInCelcius;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        WeatherWidget.updateAppWidget(getContext(), appWidgetManager, MainActivity.mAppWidgetId);

    }

    private void setWeatherIcon(String iconCode) {
        Drawable drawable;
        switch (iconCode) {
            case "01d":
                drawable = getResources().getDrawable(R.drawable.sunny);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_day));
                weatherState = "clear sky-day";
                break;
            case "01n":
                drawable = getResources().getDrawable(R.drawable.clear_night);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_night));
                weatherState = "clear sky-night";
                break;
            case "02d":
                drawable = getResources().getDrawable(R.drawable.cloudy_sunny);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_day));
                weatherState = "few clouds-day";
                break;
            case "02n":
                drawable = getResources().getDrawable(R.drawable.moon_night_cloudy);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_night));
                weatherState = "few clouds-night";
                break;
            case "03d":
                drawable = getResources().getDrawable(R.drawable.cloudy);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_day));
                weatherState = "scattered clouds-day";
                break;
            case "03n": //g
                drawable = getResources().getDrawable(R.drawable.cloudy);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_night));
                weatherState = "scattered clouds-night";
                break;
            case "04d":
                drawable = getResources().getDrawable(R.drawable.broken_clouds);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_thunderstorm));
                weatherState = "broken clouds-day";
                break;
            case "04n":
                drawable = getResources().getDrawable(R.drawable.broken_clouds);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_thunderstorm));
                weatherState = "broken clouds-night";
                break;
            case "09d":
                drawable = getResources().getDrawable(R.drawable.shower_rain);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_thunderstorm));
                weatherState = "shower rain-day";
                break;
            case "09n":
                drawable = getResources().getDrawable(R.drawable.shower_rain);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_thunderstorm));
                weatherState = "shower rainnight";
                break;
            case "10d":
                drawable = getResources().getDrawable(R.drawable.day_sun_rain);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_day));
                weatherState = "rain-day";
                break;
            case "10n":
                drawable = getResources().getDrawable(R.drawable.nigh_moon_rain);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_night));
                weatherState = "rain-night";
                break;
            case "11d":
                drawable = getResources().getDrawable(R.drawable.thunderstorm);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_thunderstorm));
                weatherState = "thunderstorm-day";
                break;
            case "11n":
                drawable = getResources().getDrawable(R.drawable.thunderstorm);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_thunderstorm));
                weatherState = "thunderstorm-night";
                break;
            case "13d":
                drawable = getResources().getDrawable(R.drawable.snow);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_snow_mist));
                weatherState = "snow-day";
                break;
            case "13n":
                drawable = getResources().getDrawable(R.drawable.snow);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_snow_mist));
                weatherState = "snow-night";
                break;
            case "50d":
                drawable = getResources().getDrawable(R.drawable.mist);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_snow_mist));
                weatherState = "mist-day";
                break;
            case "50n":
                drawable = getResources().getDrawable(R.drawable.mist);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_snow_mist));
                weatherState = "mist-night";
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.error);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.gradient_error));
                weatherState = "error-night";
                break;
        }
        weatherIcon.setImageDrawable(drawable);
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }

    public void addElement(String city) {
        CityDataSource.addCity(city);
        dataUpdated();
    }

    private void dataUpdated() {
        elements.clear();
        elements.addAll(cityDataSource.getAllCities());

    }

}

