package com.example.firstnews;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    final int NEWS_NOTIF = 3;
    final int WEATHER_NOTIF = 4;
    final int PENDING_ID =5;
    Notification notification;
    NotificationManager notificationManager;
    Notification.Builder builder;
    News news;
    Weather weather;
    String city;

    FusedLocationProviderClient client;
    Geocoder geocoder;
    final String BASE_LINK_NEWS = "http://newsapi.org/v2/top-headlines?country=il&category=sports&apiKey=77d0acf9be214ed4b7c4c438e081d389";
    final  String BASE_LINK_WEATHER = "http://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
    final String BASE_URL_IMG = "http://openweathermap.org/img/w/";

    @Override
    public void onReceive(final Context context, Intent intent) {

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

        switch(choiceKind) {
            case("0"):

                news = NewsFragment.getLastNews();

                builder = new Notification.Builder(context);
                notification = builder.setContentTitle(news.getTitle())
                        .setContentText(news.getDescription()).setAutoCancel(true).setSmallIcon(R.drawable.news_icon).build();
                notification.defaults = Notification.DEFAULT_VIBRATE;
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NEWS_NOTIF, notification);
                break;
            case("1"):

                weather = WeatherFragment.getLastWeather();
                city = WeatherFragment.getLastLocation();

                builder = new Notification.Builder(context);
                notification = builder.setContentTitle("מזג האוויר ב"+city)
                        .setContentText(weather.getDescription()+" "+weather.getCelsius())
                        .setAutoCancel(true).setSmallIcon(R.drawable.news_icon).build();
                notification.defaults = Notification.DEFAULT_VIBRATE;
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(WEATHER_NOTIF, notification);

                break;

        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time * 1000, pendingIntent);

            /*if (choiceKind.equals(R.string.last_news)) {
                //News
            }
             else {
                //Weather
            }*/
    }
}
