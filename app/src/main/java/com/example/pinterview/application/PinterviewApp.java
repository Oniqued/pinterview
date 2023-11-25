package com.example.pinterview.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.pinterview.data.DBHelper;

public class PinterviewApp extends Application {
    public static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteDatabase db;
        dbHelper = new DBHelper(PinterviewApp.this, "newdb.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }
}
