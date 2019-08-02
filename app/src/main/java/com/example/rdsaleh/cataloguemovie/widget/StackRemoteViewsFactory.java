package com.example.rdsaleh.cataloguemovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rdsaleh.cataloguemovie.BuildConfig;
import com.example.rdsaleh.cataloguemovie.model.Movie;
import com.example.rdsaleh.cataloguemovie.R;
import com.example.rdsaleh.cataloguemovie.database.MovieHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {


    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private MovieHelper movieHelper;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        movieHelper = new MovieHelper(mContext);
    }

    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems = new ArrayList<>();
        getMoviesFavorite();
    }

    private void getMoviesFavorite(){
        movieHelper.open();
        ArrayList<Movie> movies = movieHelper.getAllData();

            for(Movie m: movies){
                try {
                    Bitmap preview = Glide.with(mContext)
                            .asBitmap()
                            .load(BuildConfig.IMG_URL+m.getPoster_path())
                            .apply(new RequestOptions().fitCenter())
                            .submit()
                            .get();
                    mWidgetItems.add(preview);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        movieHelper.close();
    }

    @Override
    public void onDestroy() {
        mWidgetItems.clear();
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_items);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(MovieFavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
