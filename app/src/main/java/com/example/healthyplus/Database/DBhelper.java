package com.example.healthyplus.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {

    public DBhelper(@Nullable Context context) {

        super(context, "healthyPlusDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //   sqLiteDatabase.execSQL("Drop table if exists tableName");
        //   onCreate(sqLiteDatabase);
    }
}
