package com.example.rdsaleh.cataloguemovie;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toolbar;

import com.example.rdsaleh.cataloguemovie.database.AppPreference;
import com.example.rdsaleh.cataloguemovie.service.DailyReminder;
import com.example.rdsaleh.cataloguemovie.service.ReleaseReminder;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout dailyNotif, releaseNotif;
    private Switch dailySwitch, releaseSwitch;

    private Boolean boolDaily, boolRelease;
    private AppPreference appPreference;
    private DailyReminder dailyReminder;
    private ReleaseReminder releaseReminder;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContext = this;

        dailyNotif   = findViewById(R.id.dailyNotif);
        releaseNotif = findViewById(R.id.releaseNotif);

        dailySwitch   = findViewById(R.id.switchDaily);
        releaseSwitch = findViewById(R.id.switchRelease);

        dailyReminder   = new DailyReminder();
        releaseReminder = new ReleaseReminder();
        appPreference   = new AppPreference(this);

        enableDisableNotification();

        dailySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolDaily = dailySwitch.isChecked();
                if (boolDaily){
                    dailySwitch.setEnabled(true);
                    appPreference.setDailyReminderMovie(boolDaily);
                    dailyReminder.repeatingDailyReminder(mContext, DailyReminder.KEY_REPEATING, "07:00", getString(R.string.daily_text));
                }else {
                    dailySwitch.setChecked(false);
                    appPreference.setDailyReminderMovie(boolDaily);
                    dailyReminder.cancelDailyReminder(mContext, DailyReminder.KEY_REPEATING);
                }
            }
        });

        releaseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolRelease = releaseSwitch.isChecked();
                if(boolRelease){
                    releaseSwitch.setEnabled(true);
                    appPreference.setReleaseReminderMovie(boolRelease);
                    releaseReminder.repeatingReleaseReminder(mContext,ReleaseReminder.KEY_REPEATING, "08:00");
                }else{
                    releaseSwitch.setChecked(false);
                    appPreference.setReleaseReminderMovie(boolRelease);
                    releaseReminder.cancelReleaseReminder(mContext, ReleaseReminder.KEY_REPEATING);
                }
            }
        });

    }

    public void enableDisableNotification(){
        boolDaily = dailySwitch.isChecked();
        if (appPreference.getDailyReminderMovie()){
            dailySwitch.setChecked(true);
            appPreference.setDailyReminderMovie(boolDaily);
            dailyReminder.repeatingDailyReminder(mContext, DailyReminder.KEY_REPEATING, "07:00", getString(R.string.daily_text));
        }else {
            dailySwitch.setChecked(false);
            appPreference.setDailyReminderMovie(boolDaily);
            dailyReminder.cancelDailyReminder(mContext, DailyReminder.KEY_REPEATING);
        }

        boolRelease = releaseSwitch.isChecked();
        if (appPreference.getReleaseReminderMovie()){
            releaseSwitch.setChecked(true);
            appPreference.setReleaseReminderMovie(boolRelease);
            releaseReminder.repeatingReleaseReminder(mContext,ReleaseReminder.KEY_REPEATING, "08:00");
        }else {
            releaseSwitch.setChecked(false);
            appPreference.setReleaseReminderMovie(boolRelease);
            releaseReminder.cancelReleaseReminder(mContext, ReleaseReminder.KEY_REPEATING);
        }
    }


}
