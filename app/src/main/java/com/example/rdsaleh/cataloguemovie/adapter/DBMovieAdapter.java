package com.example.rdsaleh.cataloguemovie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.CONTENT_URI;

public class DBMovieAdapter extends RecyclerView.Adapter<DBMovieAdapter.MyViewHolder> {

    private Cursor listMovies;
    private Activity activity;

    public DBMovieAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setListMovies(Cursor listMovies) {
        this.listMovies = listMovies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_movie, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Movie movies = getItem(i);

        Glide.with(activity)
                .load(BuildConfig.IMG_URL+movies.getPoster_path())
                .into(myViewHolder.img);

        myViewHolder.title.setText(movies.getTitle());
        myViewHolder.overview.setText(movies.getOverview());
        myViewHolder.idMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movie_id = movies.getId();
                Intent intent = new Intent(activity, DetailMovieActivity.class);

                Uri uri = Uri.parse(CONTENT_URI + "/" + movies.getId());
                intent.setData(uri);

                intent.putExtra("movie_id", movie_id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listMovies == null) return  0;
        return listMovies.getCount();
    }

    private Movie getItem(int Position){
        if (!listMovies.moveToPosition(Position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(listMovies);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, overview;
        ImageView img;
        CardView idMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title    = itemView.findViewById(R.id.tv_title);
            overview = itemView.findViewById(R.id.tv_overview);
            img      = itemView.findViewById(R.id.img_photo);
            idMovie  = itemView.findViewById(R.id.cv_movie);
        }
    }

}
