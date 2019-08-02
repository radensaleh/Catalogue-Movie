package com.example.rdsaleh.cataloguemovie.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.example.rdsaleh.cataloguemovie";
    public static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class MovieColumns implements BaseColumns{
        public static String TABLE_NAME = "tb_movie";
        public static String ID_MOVIE = "id_movie";
        public static String TITLE = "title";
        public static String IMG_MOVIE = "img_movie";
        public static String OVERVIEW = "overview";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

    }

    public static String getColumnString(Cursor cursor, String columnName){
        return cursor.getString(cursor.getColumnIndex(columnName));
    }


}
