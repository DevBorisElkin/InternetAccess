package ru.startandroid.hw3_internetaccess.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CityDataSource {

    private DatabaseHelper dbHelper;
    private static SQLiteDatabase database;

    private String[] citiesAllColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_CITY,
    };

    public CityDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public static void addCity(String city) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CITY, city);
        String whereClause=getWhereClause("city", city);
        database.delete(DatabaseHelper.TABLE_OF_RECENT_CITIES, whereClause,null);
        database.insert(DatabaseHelper.TABLE_OF_RECENT_CITIES, null, values);
    }


    public void editCity(long id) {
        ContentValues editedCity = new ContentValues();
        editedCity.put(DatabaseHelper.COLUMN_ID, id);
        editedCity.put(DatabaseHelper.COLUMN_CITY, "What's that");

        database.update(DatabaseHelper.TABLE_OF_RECENT_CITIES,
                editedCity,
                DatabaseHelper.COLUMN_ID + "=" + id,
                null);
    }

    public void deleteNote(City city) {
        long id = city.getId();
        database.delete(DatabaseHelper.TABLE_OF_RECENT_CITIES, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_OF_RECENT_CITIES, null, null);
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_OF_RECENT_CITIES,
                citiesAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = cursorToCity(cursor);
            cities.add(city);
            cursor.moveToNext();
        }
        // обязательно закройте cursor
        cursor.close();
        return cities;
    }
    private City cursorToCity(Cursor cursor) {
        City city = new City();
        city.setId(cursor.getLong(0));
        city.setCity(cursor.getString(1));
        return city;
    }
    public static String getWhereClause(String compareColumn, String compareValue) {
        String whereClause = null;
        if (compareColumn == null || compareColumn == "") {
        } else if (compareValue == null || compareColumn == "") {
        } else {
            whereClause = compareColumn + "=\"" + compareValue + "\"";
        }
        return whereClause;
    }

}
