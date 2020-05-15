package com.example.firstnews;

import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WeatherJsonDeserializer implements JsonDeserializer {
    private static String TAG = WeatherJsonDeserializer.class.getName();
    String MSG="tagaim";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Weather> weathers = null;
        try{
            JsonObject helperObject = json.getAsJsonObject();
            JsonArray helperArray = json.getAsJsonArray();

            JsonObject root = json.getAsJsonObject();

            JsonObject jsonObject_list = root.getAsJsonObject("list");
            JsonArray jsonArray_list = root.getAsJsonArray("list");

            JsonArray jsonArray_city = root.getAsJsonArray("city");
            helperObject = jsonArray_city.get(0).getAsJsonObject();
            String city = helperObject.get("name").getAsString();

            //String city = String.valueOf(jsonArray_city.get(String.valueOf(0)));
            //Log.d(MSG, city);
            weathers = new ArrayList<>(jsonArray_list.size());
            for(int i=0; i<jsonArray_list.size();i++){
                Weather dematerialized = context.deserialize(jsonArray_list.get(i), Weather.class);
                /*JsonArray jsonArray_main = jsonObject_list.getAsJsonArray("main");
                helperObject = jsonArray_main.get(i).getAsJsonObject();
                Double celsius = helperObject.get("temp").getAsDouble();
                Double fahrenheit = celsius;
                String dataAndTime = jsonObject_list.get("dt_txt").getAsString();
                String date = dataAndTime.substring(5, 10);
                String time = dataAndTime.substring(11, 16);*/

                //String day;

                //String image;

                //Weather dematerialized = new Weather(date, time, celsius, fahrenheit);
                //System.out.println(dematerialized);
                weathers.add(dematerialized);
            }
        }catch(JsonParseException jpe){
        }
        return weathers;
    }
}
