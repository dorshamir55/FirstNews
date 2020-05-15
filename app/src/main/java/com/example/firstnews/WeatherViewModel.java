package com.example.firstnews;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.view.ViewDebug;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WeatherViewModel extends AndroidViewModel {
    private MutableLiveData<List<Weather>> weatherList;

    final int LOCATION_PERMISSION_REQUEST = 1;
    FusedLocationProviderClient client;
    Geocoder geocoder;
    Handler handler = new Handler();
    private Double lat;
    private Double lon;
    Context context;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Weather>> getWeatherList() {
        if(weatherList==null){
            weatherList = new MutableLiveData<>();
            loadWeathers();
        }
        return weatherList;
    }

    private void loadWeathers(){
        Api api = ApiUtil.getRetrofitApi();
        getLatiAndLongi();
        Call<List<Weather>> call = api.getWeather(lat, lon);

        call.enqueue(new Callback<List<Weather>>() {
            @Override
            public void onResponse(Response<List<Weather>> response, Retrofit retrofit) {
                weatherList.setValue(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getLatiAndLongi(){
        client = LocationServices.getFusedLocationProviderClient(getApplication().getBaseContext());
        LocationCallback callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastlocation  = locationResult.getLastLocation();
                lat = lastlocation.getLatitude();
                lon = lastlocation.getLongitude();
                //textView.setText(lastlocation.getLongitude()+" , "+lastlocation.getLatitude());
                /*new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            geocoder = new Geocoder(getApplication().getApplicationContext());
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
                }.start();*/
            }
        };
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        client.requestLocationUpdates(request, callback, null);
    }
}
