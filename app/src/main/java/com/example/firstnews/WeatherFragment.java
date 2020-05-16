package com.example.firstnews;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class WeatherFragment extends android.app.Fragment {
    private List<Weather> weatherList;
    WeatherAdapter weatheradapter;
    Context context;

    public double lat;
    public double lon;

    FusedLocationProviderClient client;
    Geocoder geocoder;
    //Handler handler = new Handler();

    final String BASE_LINK = "http://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";
    final String BASE_URL_IMG = "http://openweathermap.org/img/w/";

    /*public static WeatherFragment newInstance (int num){
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("day", num);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }*/

    public WeatherFragment(){
        weatherList = new ArrayList<>();
        this.context=getActivity();
    }

    /*public static WeatherFragment newInstance(List<Weather> i_weatherList){
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherList=i_weatherList;
        return weatherFragment;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View root = inflater.inflate(R.layout.day_fragment, container, false);
        Day day = Day.values()[getArguments().getInt("day")];
        TextView textView = root.findViewById(R.id.day_name);
        switch (day){
            case Sunday:
                textView.setText("Sunday");
                break;
            case Monday:
                textView.setText("Monday");
                break;
            case Tuesday:
                textView.setText("Tuesday");
                break;
            case Wednesday:
                textView.setText("Wednesday");
                break;
            case Thursday:
                textView.setText("Thursday");
                break;
            case Friday:
                textView.setText("Friday");
                break;
            case Saturday:
                textView.setText("Saturday");
                break;
        }*/

        //final WeatherManager manager = WeatherManager.getInstance(getActivity());

        //manager.start();

        //weatherList = manager.getWeatherList();

        startLocation();
        getWeather();

        View root = inflater.inflate(R.layout.weather_fragment, container, false);
        //SystemClock.sleep(800);
        final RecyclerView recyclerView = root.findViewById(R.id.weather_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        //weatherList = new ArrayList<Weather>();
        //String imageUri = "drawable://" + R.drawable.ic_launcher_background;
        //weatherList.add(new Weather("א'", "14.5", "3:00", 25.0, 45.0, imageUri));

        weatheradapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(weatheradapter);
        weatheradapter.notifyDataSetChanged();
        weatheradapter.notifyItemInserted(39);
        return root;
    }

    public void startLocation(){
        geocoder = new Geocoder(getActivity());
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastlocation = locationResult.getLastLocation();
                lat = lastlocation.getLatitude();
                lon = lastlocation.getLongitude();
            }
        };

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        client.requestLocationUpdates(request, callback, null);
    }

    public void getWeather() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK + "&lat=" + "34.78" + "&lon=" + "32.0" +"&units=metric"+"&lang=he", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject rootObject = new JSONObject(response);
                    JSONObject cityObject = response.getJSONObject("city");
                    String city = cityObject.getString("name");
                    //cityTv
                    //JSONObject listObject = response.getJSONObject("list");
                    JSONArray listArray = response.getJSONArray("list");
                    for(int i=0; i<listArray.length();i++){
                        JSONObject currentElementObject = listArray.getJSONObject(i);
                        String dateAndTime = currentElementObject.getString("dt_txt");
                        String date = dateAndTime.substring(5,10);
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

                        Weather weather = new Weather(dayFromDate, date, time, celsius, description, icon);
                        weatherList.add(weather);
                        weatheradapter.notifyItemInserted(i);
                    }

                    //weatherList = new ArrayList<Weather>();
                    //String imageUri = "drawable://" + R.drawable.ic_launcher_background;
                    //weatherList.add(new Weather("א'", "14.5", "3:00", 25.0, 45.0, imageUri));

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
