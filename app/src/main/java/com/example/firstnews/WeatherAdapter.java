package com.example.firstnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<Weather> weatherList;
    private final Context context;


    public WeatherAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView timeTv;
        ImageView imageIv;
        TextView celsiusTv;
        TextView descriptionTv;
        TextView dayTv;
        TextView dateTv;
        ViewHolder(View itemView){
            super(itemView);
            timeTv = itemView.findViewById(R.id.weather_time_tv);
            imageIv = itemView.findViewById(R.id.weather_image_iv);
            celsiusTv = itemView.findViewById(R.id.weather_celsius_tv);
            descriptionTv = itemView.findViewById(R.id.weather_description_tv);
            dayTv = itemView.findViewById(R.id.weather_day_tv);
            dateTv = itemView.findViewById(R.id.weather_date_tv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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