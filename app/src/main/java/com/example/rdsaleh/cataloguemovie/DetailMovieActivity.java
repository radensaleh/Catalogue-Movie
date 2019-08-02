package com.example.rdsaleh.cataloguemovie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rdsaleh.cataloguemovie.database.MovieHelper;
import com.example.rdsaleh.cataloguemovie.model.Movie;
import com.example.rdsaleh.cataloguemovie.model.MovieDetail;
import com.example.rdsaleh.cataloguemovie.retroserver.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.ID_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.IMG_MOVIE;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.rdsaleh.cataloguemovie.database.DatabaseContract.MovieColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity {

    private String api_key  = BuildConfig.API_KEY;
    private String language = "en-US";

    private String id_movie, title_movie, overview_movie, img_movie;

    private TextView tv_title, tv_tagline, tv_rating, tv_runtime, tv_language, tv_release, tv_budget, tv_overview;
    private ImageView img;
    int love = 1;

    private MovieHelper movieHelper;
    private Context mContext;
    private Movie movie;

    private Menu opMenu;

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(DetailMovieActivity.this, MainActivity.class);
//        startActivity(i);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.detail_movie);
        setContentView(R.layout.activity_detail_movie);

        mContext = this;
        movieHelper = new MovieHelper(mContext);

        tv_title    = findViewById(R.id.tv_title);
        tv_tagline  = findViewById(R.id.tv_tagline);
        tv_rating   = findViewById(R.id.tv_rating);
        tv_runtime  = findViewById(R.id.tv_runtime);
        tv_language = findViewById(R.id.tv_language);
        tv_release  = findViewById(R.id.tv_release);
        tv_budget   = findViewById(R.id.tv_budget);
        tv_overview = findViewById(R.id.tv_overview);

        opMenu = findViewById(R.id.action_favorite);
        img    = findViewById(R.id.img);

        showDetailMovie();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String movie_id = (String) b.get("movie_id");

        //cek id
        movieHelper.open();
        if(movieHelper.CekID(movie_id)){
            love = 2;
        }else if(!movieHelper.CekID(movie_id)){
            love = 1;
        }

        //uri
        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
            }
        }

        movieHelper.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(love == 1){
            getMenuInflater().inflate(R.menu.favorite, menu);
        }else if(love == 2){
            getMenuInflater().inflate(R.menu.favorite_red, menu);
        }

        this.opMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            if(love % 2 == 1){
                //add

                ContentValues values = new ContentValues();
                values.put(ID_MOVIE, id_movie);
                values.put(TITLE, title_movie);
                values.put(IMG_MOVIE, img_movie);
                values.put(OVERVIEW, overview_movie);
                getContentResolver().insert(CONTENT_URI, values);
                opMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24dp));

                love += 1;

            }else if(love % 2 == 0){
                //delete

                getContentResolver().delete(getIntent().getData(), null, null);

                opMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24dp));
                love -= 1;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDetailMovie(){
        Intent i = getIntent();
        Bundle b = i.getExtras();

        String movie_id = (String) b.get("movie_id");

        Call<MovieDetail> detailMovieCall = Retrofit
                .getInstance()
                .baseAPI()
                .getDetailMovie(movie_id, api_key, language);

        detailMovieCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                String title    = response.body().getTitle();
                String tagline  = response.body().getTagline();
                String rating   = response.body().getVote_average();
                int runtime     = response.body().getRuntime();
                String language = response.body().getOriginal_language();
                String release  = response.body().getRelease_date();
                int budget      = response.body().getBudget();
                String overview = response.body().getOverview();
                String poster   = response.body().getPoster_path();


                Glide.with(mContext)
                        .load(BuildConfig.IMG_URL+poster)
                        .into(img);

                tv_title.setText(title);
                tv_tagline.setText(tagline);
                tv_rating.setText(rating);
                tv_runtime.setText(String.valueOf(runtime));
                tv_language.setText(language.toUpperCase());
                tv_release.setText(release);
                tv_budget.setText(String.valueOf(budget));
                tv_overview.setText(overview);

                id_movie = response.body().getId();
                title_movie = title;
                overview_movie = overview;
                img_movie = poster;

            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("Error : ", t.toString());
            }
        });


    }
}
