package com.example.rdsaleh.cataloguemovie.api;
import com.example.rdsaleh.cataloguemovie.model.MovieDetail;
import com.example.rdsaleh.cataloguemovie.model.ResultMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseAPI {

    //untuk get movies yang now playing
    @GET("movie/now_playing")
    Call<ResultMovie> getMoviePlaying(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    //untuk get movies yang upcoming
    @GET("movie/upcoming")
    Call<ResultMovie> getMovieUpcoming(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );

    //untuk get movies yang top rated
    @GET("movie/top_rated")
    Call<ResultMovie> getMovieTopRated(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );


    //untuk get movies dari pencarian
    @GET("search/movie")
    Call<ResultMovie> getMovieSearch(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("query") String query
    );

    //untuk get detail movie
    @GET("movie/{movie_id}")
    Call<MovieDetail> getDetailMovie(
            @Path("movie_id") String movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );

}
