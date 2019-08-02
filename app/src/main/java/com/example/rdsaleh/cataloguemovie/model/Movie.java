package com.example.rdsaleh.cataloguemovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.rdsaleh.cataloguemovie.database.DatabaseContract;

import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable{

    private String id;
    private String title;
    private String poster_path;
    private String overview;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
    }

    public Movie() {

    }

    public Movie(Cursor cursor) {
        this.id = getColumnString(cursor, ID_MOVIE);
        this.title = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.overview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.poster_path = getColumnString(cursor, DatabaseContract.MovieColumns.IMG_MOVIE);
    }

    protected Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
