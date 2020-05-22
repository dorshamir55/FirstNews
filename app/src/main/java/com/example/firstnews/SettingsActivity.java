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

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    final int PENDING_ID = 5;
    AlarmManager alarmManager;
    android.os.Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, new SettingsFragment()).commit();
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Intent intent = new Intent(SettingsActivity.this, NotificationReceiver.class);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int time=0;
        SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String choiceTime = sp.getString("notification_time", "2");
        boolean checked = sp.getBoolean("notification_active", false);
        boolean flagToastActive = true;
        boolean flafToastCancel = true;

        if(checked) {
            if(flagToastActive) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingsActivity.this, R.string.notification_active, Toast.LENGTH_SHORT).show();
                    }
                });
                flafToastCancel = true;
                flagToastActive = false;
            }
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
                    time = 10;
                    break;
            }

            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time * 1000, pendingIntent);
        }
        else{
            if(flafToastCancel) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingsActivity.this, R.string.notification_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
                flafToastCancel = false;
                flagToastActive = true;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }
}
