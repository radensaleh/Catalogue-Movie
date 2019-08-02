package com.example.rdsaleh.cataloguemovie.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rdsaleh.cataloguemovie.R;
import com.example.rdsaleh.cataloguemovie.adapter.DBMovieAdapter;
import com.example.rdsaleh.cataloguemovie.model.Movie;

import java.util.ArrayList;

import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context mContext;
    private RecyclerView.LayoutManager layoutManager;

    private DBMovieAdapter dbMovieAdapter;
    private Cursor list;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    private ArrayList<Movie> movies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        progressBar = view.findViewById(R.id.progressbar);
        tvEmpty  = view.findViewById(R.id.tv_empty);
        recyclerView  = view.findViewById(R.id.recyclerMovie);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movies");
            dbMovieAdapter = new DBMovieAdapter(getActivity());
            dbMovieAdapter.setListMovies(list);
            recyclerView.setAdapter(dbMovieAdapter);

            new LoadMovieAsync().execute();
        } else {
            showAllMovieFav();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

    public void showAllMovieFav(){
            dbMovieAdapter = new DBMovieAdapter(getActivity());
            dbMovieAdapter.setListMovies(list);
            recyclerView.setAdapter(dbMovieAdapter);

            new LoadMovieAsync().execute();
    }

    private class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list = notes;
            dbMovieAdapter.setListMovies(list);
            dbMovieAdapter.notifyDataSetChanged();

            if (list.getCount() == 0) {
                tvEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

}
