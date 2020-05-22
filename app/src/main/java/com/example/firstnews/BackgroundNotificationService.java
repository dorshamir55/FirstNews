package com.example.firstnews;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackgroundNotificationService extends IntentService {

    final int PENDING_ID = 5;
    AlarmManager alarmManager;
    android.os.Handler handler = new Handler();



    public BackgroundNotificationService() {
        super("BackgroundNotificationService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent intent1 = new Intent(BackgroundNotificationService.this, NotificationReceiver.class);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int time=0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String choiceTime = sp.getString("notification_time", "2");
        boolean checked = sp.getBoolean("notification_active", false);

        if(checked) {
            handler.post(new Runnable() {
                @Override
                public void run () {
                    Toast.makeText(BackgroundNotificationService.this, R.string.notification_active, Toast.LENGTH_SHORT).show();
                }
            });            switch (choiceTime) {
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

            PendingIntent pendingIntent = PendingIntent.getBroadcast(BackgroundNotificationService.this, PENDING_ID, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time * 1000, pendingIntent);
        }
        else{
            handler.post(new Runnable() {
                @Override
                public void run () {
                    Toast.makeText(BackgroundNotificationService.this, R.string.notification_cancel, Toast.LENGTH_SHORT).show();
                }
            });            PendingIntent pendingIntent = PendingIntent.getBroadcast(BackgroundNotificationService.this, PENDING_ID, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            stopService(intent);
        }
    }
}
