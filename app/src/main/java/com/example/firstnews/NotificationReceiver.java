package com.example.firstnews;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    final int NOTIFICATION_ID = 3;
    final int PENDING_ID =4;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String choiceTime = sp.getString("notification_time", "0");
        String choiceKind = sp.getString("notification_kind", "0");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        int time=0;
        alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        switch (choiceTime) {
            case "0":
                //60 sec
                time = 60;
                break;
            case "1":
                //30 min
                time = 30*60;
                break;
            case "2":
                //1 hour
                time = 10 ;
                break;
        }

        Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show();
        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = builder.setContentTitle("Title")
                .setContentText("Content").setAutoCancel(true).setSmallIcon(R.drawable.news_icon).build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time * 1000, pendingIntent);


            /*if (choiceKind.equals(R.string.last_news)) {
                //News
            }
             else {
                //Weather
            }*/
    }
}