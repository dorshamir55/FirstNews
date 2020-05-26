package com.example.firstnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class WeatherFragment extends Fragment {
    private static List<Weather> weatherList;
    static WeatherAdapter weatherAdapter;
    static Context context;

    static String city;
    private String lat;
    private String lon;

    static final String BASE_LINK = "http://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
    static final String BASE_URL_IMG = "http://openweathermap.org/img/w/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("tag", "onCreateView - Weather");
        View root = inflater.inflate(R.layout.weather_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.weather_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        Log.d("tag", "After RecyclerView");
        SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        lat = sp.getString("latitude","32.0905");
        lon = sp.getString("longitude","34.7749");
        Log.d("tag", lat+", "+lon);
        weatherList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
        recyclerView.setAdapter(weatherAdapter);

        getWeather();

        return root;
    }

    private void getWeather() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK + "&lat=" + lat + "&lon=" + lon +"&units=metric"+"&lang=he", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject cityObject = response.getJSONObject("city");
                            city = cityObject.getString("name");
                            JSONArray listArray = response.getJSONArray("list");
                            int i;
                            for(i=0; i<listArray.length();i++){
                                JSONObject currentElementObject = listArray.getJSONObject(i);
                                Weather weather = new Weather();

                                String dateAndTime = currentElementObject.getString("dt_txt");
                                String date = dateAndTime.substring(8,10)+"."+dateAndTime.substring(5,7);
                                if(date.substring(3,4).equals("0")){
                                    date = date.substring(0,3)+date.substring(4,5);
                                }

                                String time = dateAndTime.substring(11,16);
                                weather.setDate(date);
                                weather.setTime(time);

                                JSONObject mainObject = currentElementObject.getJSONObject("main");
                                Double cel = Double.parseDouble(mainObject.getString("temp"));
                                String celsius = "\u2103"+cel;
                                weather.setCelsius(celsius);
                                JSONObject weatherObject = currentElementObject.getJSONArray("weather").getJSONObject(0);
                                String description = weatherObject.getString("description");
                                weather.setDescription(description);
                                String icon = BASE_URL_IMG+weatherObject.getString("icon")+".png";
                                weather.setImage(icon);
                                String year = dateAndTime.substring(0,4);
                                String day = dateAndTime.substring(8,10);
                                String month = dateAndTime.substring(5,7);

                                String dayFromDate = Day.getDayFromDate(day, month, year);
                                weather.setDay(dayFromDate);
                                weatherList.add(weather);
                            }

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor prefEditor = sp.edit();
                            prefEditor.putString("city_weather", city);
                            prefEditor.commit();

                            //lastWeather = weatherList.get(0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        weatherAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
