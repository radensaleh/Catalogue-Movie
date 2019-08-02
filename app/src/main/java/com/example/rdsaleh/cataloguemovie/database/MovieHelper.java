package com.example.rdsaleh.cataloguemovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.rdsaleh.cataloguemovie.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.IMG_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.TABLE_NAME;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.TITLE;


public class MovieHelper {

    private static String DATABASE_TABLE = TABLE_NAME;
    private Context mContext;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context mContext){
        this.mContext = mContext;
    }

    public MovieHelper open() throws SQLException{
        databaseHelper = new DatabaseHelper(mContext);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public boolean CekID(String id) {
        String Query = "Select * from " + DATABASE_TABLE + " where " + ID_MOVIE + " = " + id;
        Cursor cursor = database.rawQuery(Query, null);
        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    public ArrayList<Movie> getAllData(){
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<Movie> arrayList = new ArrayList<>();
        Movie movie;

        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID_MOVIE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(IMG_MOVIE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                arrayList.add(movie);
                cursor.moveToNext();


            } while (!cursor.isAfterLast());
        }


        cursor.close();
        return arrayList;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , ID_MOVIE + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID_MOVIE + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ID_MOVIE + " = ?", new String[]{id});
    }


}
