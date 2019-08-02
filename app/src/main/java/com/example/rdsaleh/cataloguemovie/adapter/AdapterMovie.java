package com.example.rdsaleh.cataloguemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rdsaleh.cataloguemovie.BuildConfig;
import com.example.rdsaleh.cataloguemovie.DetailMovieActivity;
import com.example.rdsaleh.cataloguemovie.model.Movie;
import com.example.rdsaleh.cataloguemovie.R;

import java.util.ArrayList;

import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.MyViewHolder> {

    private ArrayList<Movie> movies;
    private Context mContext;


    public AdapterMovie(ArrayList<Movie> movies, Context mContext) {
        this.movies = movies;
        this.mContext = mContext;
    }

    public ArrayList<Movie> getMovies(){
        return movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_movie, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        Glide.with(mContext)
                .load(BuildConfig.IMG_URL+getMovies().get(i).getPoster_path())
                .into(myViewHolder.img);

        myViewHolder.title.setText(movies.get(i).getTitle());
        myViewHolder.overview.setText(movies.get(i).getOverview());
        myViewHolder.id_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movie_id = movies.get(i).getId();
                Intent intent = new Intent(mContext, DetailMovieActivity.class);

                Uri uri = Uri.parse(CONTENT_URI + "/" + movies.get(i).getId());
                intent.setData(uri);

                intent.putExtra("movie_id", movie_id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, overview;
        ImageView img;
        CardView id_movie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title    = itemView.findViewById(R.id.tv_title);
            overview = itemView.findViewById(R.id.tv_overview);
            img      = itemView.findViewById(R.id.img_photo);
            id_movie = itemView.findViewById(R.id.cv_movie);

        }
    }

}
