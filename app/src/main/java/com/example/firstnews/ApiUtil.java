package com.example.firstnews;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiUtil {

    static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?appid=2f976482fabfb93ba421d2df01470e6c";

    public static Api getRetrofitApi(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter((new ArrayList<Weather>()).getClass(), new WeatherJsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit
                .Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Api api = retrofit.create(Api.class);
        return api;
    }
}
