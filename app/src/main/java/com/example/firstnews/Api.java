package com.example.firstnews;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Api {
    @GET(".")
    Call<List<Weather>> getWeather(@Query("lat") Double lat, @Query("lon") Double lon);

}
