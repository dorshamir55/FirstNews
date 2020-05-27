package com.example.firstnews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    static RecyclerView recyclerView;
    static Context context;
    static TextView textView;

    final int LOCATION_PERMISSION_REQUEST = 1;

    static String city;
    private String lat;
    private String lon;

    static Menu tempMenu;
    static MenuItem permission;

    static final String BASE_LINK = "https://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
    static final String BASE_URL_IMG = "https://openweathermap.org/img/w/";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        Log.d("tag", "onCreateView - Weather");
        View root = inflater.inflate(R.layout.weather_fragment, container, false);
        recyclerView = root.findViewById(R.id.weather_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        Log.d("tag", "After RecyclerView");

        if(Build.VERSION.SDK_INT>=23){
            Log.d("tag", "if");
            int hasLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(hasLocationPermission != getActivity().getPackageManager().PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
            else {
                Log.d("tag", "Have permissions");
                //startLocation();
                weatherList = new ArrayList<>();
                Log.d("tag","After define weatherList");
                weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
                Log.d("tag","After define weatherAdapter");
                recyclerView.setAdapter(weatherAdapter);
                Log.d("tag","After setAdapter");

                startLocation();
            }
        }
        else {
            //startLocation();
            weatherList = new ArrayList<>();
            Log.d("tag","After define weatherList");
            weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
            Log.d("tag","After define weatherAdapter");
            recyclerView.setAdapter(weatherAdapter);
            Log.d("tag","After setAdapter");

            startLocation();

        }

        return root;
    }

    /*private static void getForecast(String lat, String lon){
                getWeather(lat , lon);
                Log.d("tag", "After getWeather");
    }*/

    public static void startLocation(){
        Log.d("tag", "startLocation");
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        Log.d("tag", "1");
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("tag", "Before super method");
                super.onLocationResult(locationResult);
                Log.d("tag", "onLocationResult");
                Location lastLocation = locationResult.getLastLocation();
                Log.d("tag", "lastLocation");
                getWeather(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));
                /*SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("latitude", String.valueOf(lastLocation.getLatitude()));
                editor.putString("longitude", String.valueOf(lastLocation.getLongitude()));
                editor.apply();*/
            }
        };
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        client.requestLocationUpdates(request, callback, null);


    }

    /*private void getLatAndLon(){
        SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        lat = sp.getString("latitude","32.0905");
        lon = sp.getString("longitude","34.7749");
        Log.d("tag", lat+", "+lon);
    }*/

    private static void getWeather(String lat, String lon) {
        Log.d("tag","getWeather");
        Log.d("tag", lat+", "+lon);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK + "&lat=" + lat + "&lon=" + lon + "&units=metric" + "&lang=he", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag","1");
                        try {
                            Log.d("tag","onResponse");
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

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor prefEditor = sp.edit();
                            prefEditor.putString("city_weather", city);
                            prefEditor.commit();

                            //lastWeather = weatherList.get(0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        weatherAdapter.notifyDataSetChanged();
                        Log.d("tag", "notifyDataSetChanged");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.permission_title).setMessage(R.string.permission_msg)
                        .setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:"+getActivity().getPackageName()));
                                startActivity(intent);
                                //Need to loading!
                            }
                        })
                        .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                permission = tempMenu.findItem(R.id.permission_location_settings);
                                permission.setVisible(true);


                                //getFragmentManager().beginTransaction().add(R.id.frame_container1, WeatherFragment.getInstance(this), "weather_fragment").commit();

                            }
                        }).setCancelable(false).show();
            }
            else{
                //startLocation();
                weatherList = new ArrayList<>();
                Log.d("tag","After define weatherList");
                weatherAdapter = new WeatherAdapter(getActivity(), weatherList);
                Log.d("tag","After define weatherAdapter");
                recyclerView.setAdapter(weatherAdapter);
                Log.d("tag","After setAdapter");

                startLocation();
                setCity();
            }
        }
    }

    public void setCity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                TextView textView = getActivity().findViewById(R.id.weather_title_tv);
                textView.setText("מזג האוויר ב"+sp.getString("city_weather", "מיקומך"));
            }
        },1000);
    }

}
