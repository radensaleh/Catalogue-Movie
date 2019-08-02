package com.example.moviefavorite.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviefavorite.BuildConfig;
import com.example.moviefavorite.R;

import java.io.InputStream;
import java.net.URL;

import static com.example.moviefavorite.database.DatabaseContract.MovieColumns.IMG_MOVIE;
import static com.example.moviefavorite.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.moviefavorite.database.DatabaseContract.MovieColumns.TITLE;
import static com.example.moviefavorite.database.DatabaseContract.getColumnString;

public class AdapterMovieFav extends CursorAdapter {

    public AdapterMovieFav(Context mContext, Cursor cursor, Boolean autoRequery){
        super(mContext,cursor,autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.data_movie, parent, false);
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null){
            TextView tvTitle    = view.findViewById(R.id.tv_title);
            TextView tvOverview = view.findViewById(R.id.tv_overview);
            ImageView img       = view.findViewById(R.id.img_photo);

            tvTitle.setText(getColumnString(cursor,TITLE));
            String imgURL = BuildConfig.IMG_URL + getColumnString(cursor, IMG_MOVIE);
            new DownLoadImage(img).execute(imgURL);
            tvOverview.setText(getColumnString(cursor,OVERVIEW));


        }
    }

    //download image dari internet
    public static class DownLoadImage extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImage(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
