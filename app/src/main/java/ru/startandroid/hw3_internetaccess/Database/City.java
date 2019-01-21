package ru.startandroid.hw3_internetaccess.Database;

import android.text.TextUtils;

public class City {
    private long id;
    private String city;
    int position;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public long getId() {
        return id;
    }
    public String getCity() {
        return city;
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(city);
        return builder.toString();
    }


}
