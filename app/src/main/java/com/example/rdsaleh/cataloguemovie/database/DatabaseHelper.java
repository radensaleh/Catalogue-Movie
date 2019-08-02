package com.example.rdsaleh.cataloguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.IMG_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.TABLE_NAME;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.TITLE;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "movie";

    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_MOVIE = "create table "+TABLE_NAME+
            " ("+_ID+" integer primary key autoincrement, " +
            ID_MOVIE+" text not null, " +
            TITLE+" text not null, " +
            IMG_MOVIE+" text not null, " +
            OVERVIEW+" text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
