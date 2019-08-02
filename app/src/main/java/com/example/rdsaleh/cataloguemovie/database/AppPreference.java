package com.example.rdsaleh.cataloguemovie.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreference {

    private SharedPreferences sharedPreferences;
    private Context mContex;

    private String KEY_DAILY   = "daily";
    private String KEY_RELEASE = "release";

    public AppPreference(Context mContex) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContex);
        this.mContex = mContex;
    }

    public void setDailyReminderMovie(Boolean bool){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_DAILY, bool);
        editor.commit();
    }

    public Boolean getDailyReminderMovie(){ return sharedPreferences.getBoolean(KEY_DAILY, true); }

    public void setReleaseReminderMovie(Boolean bool){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_RELEASE, bool);
        editor.commit();
    }

    public Boolean getReleaseReminderMovie(){ return sharedPreferences.getBoolean(KEY_RELEASE, true); }

}
