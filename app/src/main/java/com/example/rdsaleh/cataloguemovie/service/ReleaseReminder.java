package com.example.rdsaleh.cataloguemovie.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.rdsaleh.cataloguemovie.BuildConfig;
import com.example.rdsaleh.cataloguemovie.DetailMovieActivity;
import com.example.rdsaleh.cataloguemovie.R;
import com.example.rdsaleh.cataloguemovie.model.Movie;
import com.example.rdsaleh.cataloguemovie.model.ResultMovie;
import com.example.rdsaleh.cataloguemovie.retroserver.Retrofit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseReminder extends BroadcastReceiver {

    public static final String KEY_ONE_TIME  = "OneTime";
    public static final String KEY_REPEATING = "Repeating";
    public static final String KEY_TYPE      = "type";

    public static final String KEY_ID_MOVIE  = "movie_id";

    private final int KEY_NOTIF_ID_REPEATING = 101;

    private String api_key  = BuildConfig.API_KEY;
    private String language = "en-US";
    private int page        = 1;

    private ArrayList<Movie> movies;

    @Override
    public void onReceive(Context mContext, Intent intent) {

        loadMovie(mContext);

    }

    private void loadMovie(final Context mContext){
        Call<ResultMovie> resultMovieCall = Retrofit.getInstance()
                .baseAPI()
                .getMovieUpcoming(api_key, language, page);

        resultMovieCall.enqueue(new Callback<ResultMovie>() {
            @Override
            public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                if (response.isSuccessful()) {
                    ResultMovie result = response.body();
                    movies = new ArrayList<>(Arrays.asList(result.getMovies()));

                    int index = new Random().nextInt(movies.size());

                    String idMovie = movies.get(index).getId();
                    String title = movies.get(index).getTitle();
                    int notifId = 200;

                    releaseReminder(mContext, idMovie, title, notifId);
                }
            }

            @Override
            public void onFailure(Call<ResultMovie> call, Throwable t) {

            }
        });


    }

    private void releaseReminder(Context mContext, String idMovie, String title, int notifId) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Release Reminder channel";

        NotificationManager notifManagerCompat = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(mContext, DetailMovieActivity.class);
        intent.putExtra(KEY_ID_MOVIE, idMovie);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(title)
                .setContentText(title+" "+mContext.getString(R.string.release_text))
                .setColor(ContextCompat.getColor(mContext, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);

            if (notifManagerCompat != null) {
                notifManagerCompat.createNotificationChannel(channel);
            }
        }
        if (notifManagerCompat != null) {
            notifManagerCompat.notify(notifId, builder.build());
        }

    }

    public void repeatingReleaseReminder(Context mContext, String type, String time) {
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(mContext, ReleaseReminder.class);
        i.putExtra(KEY_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        cal.set(Calendar.SECOND, 0);

        if (cal.before(Calendar.getInstance())) cal.add(Calendar.DATE, 1);

        int requestCode = KEY_NOTIF_ID_REPEATING;
        PendingIntent pi = PendingIntent.getBroadcast(mContext, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }

    public void cancelReleaseReminder(Context mContext, String type) {
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(mContext, ReleaseReminder.class);

        int requestCode = type.equalsIgnoreCase(KEY_ONE_TIME) ? KEY_NOTIF_ID_REPEATING : KEY_NOTIF_ID_REPEATING;
        PendingIntent pi = PendingIntent.getBroadcast(mContext, requestCode, i, 0);

        am.cancel(pi);
    }
}
