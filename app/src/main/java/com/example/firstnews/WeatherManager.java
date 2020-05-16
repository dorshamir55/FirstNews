package com.example.firstnews;

import android.app.DownloadManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.List;

public class WeatherManager {
    public static WeatherManager instance;
    Context context;
    List<Weather> weatherList = new ArrayList<>();

    public double lat;
    public double lon;

    FusedLocationProviderClient client;
    Geocoder geocoder;
    //Handler handler = new Handler();

    final String BASE_LINK = "http://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
    final String BASE_URL_IMG = "http://openweathermap.org/img/w/";

    public WeatherManager(Context context) {
        this.context = context;
        //startLocation();
        //getWeather();
    }

    public static WeatherManager getInstance(Context context){
        if(instance==null){
            instance = new WeatherManager(context);
        }
        return  instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void startLocation(){
        geocoder = new Geocoder(context);
        client = LocationServices.getFusedLocationProviderClient(context);
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastlocation = locationResult.getLastLocation();
                lat = lastlocation.getLatitude();
                lon = lastlocation.getLongitude();
            }
        };

                /*//textView.setText(lastlocation.getLongitude()+" , "+lastlocation.getLatitude());
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            List<Address> address = geocoder.getFromLocation(lat, lon, 1);
                            final Address bestAddress = address.get(0);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //textView.setText(lat+", "+lon);
                                    //textView.setText(bestAddress.getCountryName() +", "+bestAddress.getFeatureName());
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        };*/
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        client.requestLocationUpdates(request, callback, null);
    }

    public void getWeather() {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK + "&lat=" + lat + "&lon=" + lon+"&units=metric"+"&lang=he", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject rootObject = new JSONObject(response);
                    JSONObject cityObject = response.getJSONObject("city");
                    String city = cityObject.getString("name");
                    //JSONObject listObject = response.getJSONObject("list");
                    JSONArray listArray = response.getJSONArray("list");
                    for(int i=0; i<listArray.length();i++){
                        JSONObject currentElementObject = listArray.getJSONObject(i);
                        String dateAndTime = currentElementObject.getString("dt_txt");
                        String date = dateAndTime.substring(5,10);
                        String time = dateAndTime.substring(11,16);

                        JSONObject mainObject = currentElementObject.getJSONObject("main");
                        Double cel = Double.parseDouble(mainObject.getString("temp"));
                        String celsius = cel+"\u2103";

                        JSONObject weatherObject = currentElementObject.getJSONArray("weather").getJSONObject(0);
                        String description = weatherObject.getString("description");

                        String icon = BASE_URL_IMG+weatherObject.getString("icon")+".png";

                        String year = dateAndTime.substring(0,4);
                        String day = dateAndTime.substring(8,10);
                        String month = dateAndTime.substring(5,7);

                        String dayFromDate = Day.getDayFromDate(day, month, year);

                        Weather weather = new Weather(dayFromDate, date, time, celsius, description, icon);
                        weatherList.add(weather);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();
    }
}
