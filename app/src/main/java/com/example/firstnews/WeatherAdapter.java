package com.example.firstnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<Weather> weatherList;

    public WeatherAdapter(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView timeTv;
        ImageView imageIv;
        TextView celsiusTv;
        TextView descriptionTv;
        TextView dayTv;
        TextView dateTv;

        WeatherViewHolder(View v){
            super(v);
            timeTv = (TextView)v.findViewById(R.id.time_tv);
            imageIv = (ImageView)v.findViewById(R.id.image_iv);
            celsiusTv = (TextView)v.findViewById(R.id.celsius_tv);
            descriptionTv = (TextView)v.findViewById(R.id.description_tv);
            dayTv = (TextView)v.findViewById(R.id.day_tv);
            dateTv = (TextView)v.findViewById(R.id.date_tv);
        }
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_cell, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.timeTv.setText(weather.getTime());
        Picasso.get().load(weather.getImage()).resize(300,300).into(holder.imageIv);
        holder.celsiusTv.setText(weather.getCelsius());
        holder.descriptionTv.setText(weather.getDescription());
        holder.dateTv.setText(weather.getDate());
        holder.dayTv.setText(weather.getDay());
    }

    @Override
    public int getItemCount() {
        if(weatherList!=null)
            return weatherList.size();
        else
            return 0;
    }
}
