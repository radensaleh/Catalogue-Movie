package com.example.moviefavorite;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.moviefavorite.adapter.AdapterMovieFav;

import static com.example.moviefavorite.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private AdapterMovieFav adapterMovieFav;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lv_movie);
        adapterMovieFav = new AdapterMovieFav(this, null, true);
        listView.setAdapter(adapterMovieFav);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapterMovieFav.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapterMovieFav.swapCursor(null);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
