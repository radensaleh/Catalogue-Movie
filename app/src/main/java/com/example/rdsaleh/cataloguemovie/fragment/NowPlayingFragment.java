package com.example.rdsaleh.cataloguemovie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rdsaleh.cataloguemovie.BuildConfig;
import com.example.rdsaleh.cataloguemovie.R;
import com.example.rdsaleh.cataloguemovie.model.ResultMovie;
import com.example.rdsaleh.cataloguemovie.adapter.AdapterMovie;
import com.example.rdsaleh.cataloguemovie.model.Movie;
import com.example.rdsaleh.cataloguemovie.retroserver.Retrofit;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NowPlayingFragment extends Fragment {

    private Context mContext;
    private ResultMovie resultMovie;
    private ArrayList<Movie> movies;

    private AdapterMovie adapterMovie;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private String api_key  = BuildConfig.API_KEY;
    private String language = "en-US";
    private int page        = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        recyclerView  = view.findViewById(R.id.recyclerMovie);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList("movies");
            adapterMovie = new AdapterMovie(movies, mContext);
            recyclerView.setAdapter(adapterMovie);
            adapterMovie.notifyDataSetChanged();
        } else {
            showMovies();
        }

    }

    public void showMovies(){
        Call<ResultMovie> resultMovieCall = Retrofit.getInstance()
                .baseAPI()
                .getMoviePlaying(api_key, language, page);

        resultMovieCall.enqueue(new Callback<ResultMovie>() {
            @Override
            public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                resultMovie = response.body();
                movies = new ArrayList<>(Arrays.asList(resultMovie.getMovies()));
                adapterMovie = new AdapterMovie(movies, mContext);
                recyclerView.setAdapter(adapterMovie);
                adapterMovie.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultMovie> call, Throwable t) {
                Log.e("Error : ", t.toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }
}
