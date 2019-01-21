package ru.startandroid.hw3_internetaccess.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";

    static final String TABLE_OF_RECENT_CITIES = "recent_cities"; // название таблицы в бд
    private static final int DATABASE_VERSION = 1; // версия базы данных
    private static final String DATABASE_NAME = "recent_cities.db"; // название бд

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_OF_RECENT_CITIES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CITY + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
//            db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD " + COLUMN_AUTHOR + " text (250)");
        }
    }
}
