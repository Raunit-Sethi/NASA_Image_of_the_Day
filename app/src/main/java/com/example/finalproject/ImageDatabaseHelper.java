package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ImageDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE = "ImageDatabase";
    public static final String TABLE = "Images";
    public static final int VERSION = 3;
    public static final String ID = "id";
    public static final String URL = "url";
    public static final String HD_URL = "hd";
    public static final String DATE = "date";
    public static final String IMAGE = "image";

    public ImageDatabaseHelper(@Nullable Context context) {super(context,DATABASE, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + "("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+URL+" TEXT,"+HD_URL+" TEXT,"+DATE+" TEXT,"+IMAGE+" BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
