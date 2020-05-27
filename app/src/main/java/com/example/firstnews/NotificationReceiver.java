package com.example.firstnews;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.firstnews.WeatherFragment.context;

public class NotificationReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    final int NEWS_NOTIF = 3;
    final int WEATHER_NOTIF = 4;
    final int PENDING_ID =5;
    final int OPEN_PENDING_ID = 6;
    private String lat;
    private String lon;
    private Context context;
    Notification notification;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    String city;
    NotificationManager manager;


    final String BASE_LINK_NEWS = "http://newsapi.org/v2/top-headlines?country=il&category=sports&apiKey=77d0acf9be214ed4b7c4c438e081d389";
    final  String BASE_LINK_WEATHER = "http://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
    final String BASE_URL_IMG = "http://openweathermap.org/img/w/";

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context=context;
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent openIntent = new Intent(context, MainActivity.class);
        PendingIntent openPendingIntent = PendingIntent.getActivity(context, OPEN_PENDING_ID, openIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        //Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String choiceTime = sp.getString("notification_time", "0");
        String choiceKind = sp.getString("notification_kind", "0");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String channelId = null;
        if(Build.VERSION.SDK_INT>=26){
            channelId = "some_channel_id";
            CharSequence channelName = "some_channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            manager.createNotificationChannel(notificationChannel);

            builder = new NotificationCompat.Builder(context, channelId);
            builder.setContentIntent(openPendingIntent);
        }

        int time=0;
        alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);

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

        switch(choiceKind) {
            case("0"):
                //News notification
                getLastNews(context);
                //NewsFragment.getLastNews();
                //NewsFragment newsFragment = null;
                //newsFragment.getLastNews();
                //news = NewsFragment.getLastNew();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

                        notification = builder.setContentTitle(sp.getString("title_news", "Title"))
                                .setContentText(sp.getString("description_news", "Description"))
                                .setAutoCancel(true).setSmallIcon(R.drawable.news_icon).build();
                        notification.defaults = Notification.DEFAULT_VIBRATE;
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager = (NotificationManager)
                                context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(NEWS_NOTIF, notification);

                    }
                }, 3000);

                break;

            case("1"):
                //Weather notification

//                WeatherFragment weatherFragment = WeatherFragment.getInstance(context);
//                WeatherFragment.startLastLocationAndWeather();
                //lastWeatherFunction(context);
                //SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(context);
                //lat = sp1.getString("latitude","32.0905");
                //lon = sp1.getString("longitude","34.7749");
                lastWeatherFunction(context);
                //city = WeatherFragment.getLastLocation();
                //builder = new Notification.Builder(context, channelId);
                //builder.setContentIntent(openPendingIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        notification = builder.setContentTitle("מזג האוויר ב"+sp.getString("city_weather", "מיקומך"))
                                .setContentText(sp.getString("description_weather", "שמיים בהירים")+" "+sp.getString("celsius_weather", "26.79"))
                                .setAutoCancel(true).setSmallIcon(R.drawable.news_icon).build();
                        notification.defaults = Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS;
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager = (NotificationManager)
                                context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(WEATHER_NOTIF, notification);

                    }
                }, 3000);

                break;

        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, time);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            /*if (choiceKind.equals(R.string.last_news)) {
                //News
            }
             else {
                //Weather
            }*/
    }

    private void lastWeatherFunction(final Context context) {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        LocationCallback callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastLocation=locationResult.getLastLocation();
                //SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
                //SharedPreferences.Editor editor = sp.edit();
                //editor.putString("latitude",String.valueOf(lastLocation.getLatitude()));
                //editor.putString("longitude",String.valueOf(lastLocation.getLongitude()));
                //editor.apply();
                getLastWeather(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()), context);
            }
        };
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        client.requestLocationUpdates(request,callback,null);

    }

    public void getLastNews(final Context context){
        //RequestQueue queue = Volley.newRequestQueue(context);
        String BASE_LINK = "https://newsapi.org/v2/top-headlines?country=il&category=sports&apiKey=77d0acf9be214ed4b7c4c438e081d389";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK +"", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject rootObject = new JSONObject(response);
                    //JSONObject listObject = response.getJSONObject("articles");

                    //sportTv = sportTv.findViewById(R.id.news_title_tv);
                    //sportTv.setText(R.string.sport_title);

                    JSONArray articlesArray = response.getJSONArray("articles");
                    int i=0;
                    JSONObject currentElementObject = articlesArray.getJSONObject(i);
                    while(currentElementObject.getJSONObject("source").getString("name").equals("Israelhayom.co.il")){
                        i++;
                        currentElementObject = articlesArray.getJSONObject(i);
                    }
                    if(i<articlesArray.length()) {
                        String title = currentElementObject.getString("title");

                        String date = currentElementObject.getString("publishedAt");
                        String part1 = date.substring(11, 16);
                        String part2 = date.substring(0, 4) + "." + date.substring(5, 7) + "." + date.substring(8, 10);
                        if (date.substring(5, 6).equals("0")) {
                            part2 = date.substring(8, 10) + "." + date.substring(6, 7) + "." + date.substring(0, 4);
                        }
                        if (part1.substring(0, 1).equals("0")) {
                            part1 = date.substring(12, 16);
                        }
                        date = part1 + "  " + part2;
                        String description = currentElementObject.getString("description");
                        if (description.equals("null")) {
                            description = "";
                        }

                        String icon = currentElementObject.getString("urlToImage");
                        /*if(icon.equals("null")){
                            icon = DEFAULT_ICON;
                        }*/

                        String webUrl = currentElementObject.getString("url");

                        SharedPreferences sp = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor prefEditor = sp.edit();
                        prefEditor.putString("title_news", title);
                        prefEditor.putString("description_news", description);
                        prefEditor.putString("icon_news", icon);
                        prefEditor.putString("date_news", date);
                        prefEditor.putString("webUrl_news", webUrl);
                        prefEditor.commit();
                    }

                    //lastNews = new News(title, description, icon, date, webUrl);

                    //final News lastNews = newsList.get(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public static void getLastWeather(String lati, String longi, final Context context) {
        final String BASE_LINK = "https://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
        final String BASE_URL_IMG = "https://openweathermap.org/img/w/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK + "&lat=" + lati + "&lon=" + longi +"&units=metric"+"&lang=he", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject cityObject = response.getJSONObject("city");
                    String city = cityObject.getString("name");

                    JSONArray listArray = response.getJSONArray("list");
                    JSONObject currentElementObject = listArray.getJSONObject(0);
                    String dateAndTime = currentElementObject.getString("dt_txt");
                    String date = dateAndTime.substring(8,10)+"."+dateAndTime.substring(5,7);
                    if(date.substring(3,4).equals("0")){
                        date = date.substring(0,3)+date.substring(4,5);
                    }

                    String time = dateAndTime.substring(11,16);

                    JSONObject mainObject = currentElementObject.getJSONObject("main");
                    Double cel = Double.parseDouble(mainObject.getString("temp"));
                    String celsius = "\u2103"+cel;

                    JSONObject weatherObject = currentElementObject.getJSONArray("weather").getJSONObject(0);
                    String description = weatherObject.getString("description");

                    String icon = BASE_URL_IMG+weatherObject.getString("icon")+".png";

                    String year = dateAndTime.substring(0,4);
                    String day = dateAndTime.substring(8,10);
                    String month = dateAndTime.substring(5,7);

                    String dayFromDate = Day.getDayFromDate(day, month, year);

                    SharedPreferences sp = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor prefEditor = sp.edit();
                    prefEditor.putString("dayFromDate_weather", dayFromDate);
                    prefEditor.putString("date_weather", date);
                    prefEditor.putString("time_weather", icon);
                    prefEditor.putString("celsius_weather", celsius);
                    prefEditor.putString("description_weather", description);
                    prefEditor.putString("icon_weather", icon);
                    prefEditor.putString("city_weather", city);
                    prefEditor.commit();

                    //lastWeather = new Weather(dayFromDate, date, time, celsius, description, icon);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //weatherAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

}