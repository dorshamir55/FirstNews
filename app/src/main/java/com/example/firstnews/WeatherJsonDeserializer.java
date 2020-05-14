package com.example.firstnews;

import android.util.Log;

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
    String TAGAIM="tagaim";
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Weather> weathers = null;
        try{
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray jsonArray_list = jsonObject.getAsJsonArray("list");
            JsonObject jsonObject1 = json.getAsJsonObject();
            JsonArray jsonArray_city = jsonObject1.getAsJsonArray("city");
            String city = String.valueOf(jsonArray_city.get(1));
            Log.d(TAGAIM, city);
            weathers = new ArrayList<>(jsonArray_list.size());
            for(int i=0; i<jsonArray_list.size();i++){
                Weather dematerialized = context.deserialize(jsonArray_list.get(i), Weather.class);
                weathers.add(dematerialized);
            }
        }catch(JsonParseException jpe){
        }
        return weathers;
    }
}
