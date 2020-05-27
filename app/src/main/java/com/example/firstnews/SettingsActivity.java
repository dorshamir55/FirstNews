package com.example.firstnews;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    final int PENDING_ID = 5;
    AlarmManager alarmManager;
    android.os.Handler handler = new Handler();
    boolean flagToastCancel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("notification_active", false)){
            flagToastCancel = true;
        }
        else
        {
            flagToastCancel = false;
        }
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, new SettingsFragment()).commit();
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Intent intent = new Intent(SettingsActivity.this, NotificationReceiver.class);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int time=0;
        SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String choiceTime = sp.getString("notification_time", "3");
        String choiceKind = sp.getString("notification_kind", "3" );
        boolean switched = sp.getBoolean("notification_active", false);

        if(switched & (choiceTime.equals("0") | choiceTime.equals("1") | choiceTime.equals("2")) & (choiceKind.equals("0") | choiceKind.equals("1"))) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SettingsActivity.this, R.string.notification_active, Toast.LENGTH_SHORT).show();
                }
            });
            flagToastCancel = true;
            switch (choiceTime) {
                case "0":
                    //60 sec
                    time = 60;
                    break;
                case "1":
                    //30 min
                    time = 30 * 60;
                    break;
                case "2":
                    //1 hour
                    time = 60 * 60;
                    break;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, time);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        else if ((choiceTime.equals("0") | choiceTime.equals("1") | choiceTime.equals("2")) & (choiceKind.equals("0") | choiceKind.equals("1"))){
            if(flagToastCancel) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingsActivity.this, R.string.notification_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
                flagToastCancel = false;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }
}