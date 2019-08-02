package com.example.rdsaleh.cataloguemovie.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class SearchMovieFragment extends Fragment {

    private Context mContext;
    private ResultMovie resultMovie;
    private ArrayList<Movie> movies;

    private AdapterMovie adapterMovie;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private String api_key  = BuildConfig.API_KEY;
    private String language = "en-US";
    private int page        = 1;

    private EditText et_title;
    private Button btn_search;

    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        pd = new ProgressDialog(mContext);

        recyclerView  = view.findViewById(R.id.recyclerMovie);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        et_title   = view.findViewById(R.id.et_title);
        btn_search = view.findViewById(R.id.btn_search);

        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0 ) { //ketika edit text kosong akan memanggil method showMovies()
                    showMovies();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_title.getText())){
                    new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.failed)
                            .setTitle(R.string.failed)
                            .setMessage(R.string.title_empty)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }else {
                    String title = et_title.getText().toString();
                    searchMovie(title); //method searchMovies()
                }
            }
        });

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
                .getMovieTopRated(api_key, language, page);

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

    public void searchMovie(final String title){
        pd.setIcon(R.drawable.ic_search_24dp);
        pd.setTitle(R.string.searching);
        pd.setCancelable(false);
        pd.show();

        Runnable pr = new Runnable() {
            @Override
            public void run() {
                Call<ResultMovie> resultMovieCall = Retrofit.getInstance()
                        .baseAPI()
                        .getMovieSearch(api_key,language, title);

                resultMovieCall.enqueue(new Callback<ResultMovie>() {
                    @Override
                    public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                        pd.dismiss();
                        resultMovie = response.body();
                        movies = new ArrayList<>(Arrays.asList(resultMovie.getMovies()));
                        adapterMovie = new AdapterMovie(movies, mContext);
                        recyclerView.setAdapter(adapterMovie);
                        adapterMovie.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResultMovie> call, Throwable t) {
                        pd.dismiss();
                        Log.e("Error : ", t.toString());
                    }
                });
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(pr, 2000); //waktu delay progress dialog


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movies", movies);
        super.onSaveInstanceState(outState);
    }

}
