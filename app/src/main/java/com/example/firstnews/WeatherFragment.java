package com.example.firstnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    /*public static WeatherFragment newInstance (int num){
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("day", num);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }*/

    private static List<Weather> weatherList;

    public static WeatherFragment newInstance(List<Weather> i_weatherList){
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherList=i_weatherList;
        return weatherFragment;
    }

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

        View root = inflater.inflate(R.layout.weather_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.weather_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        weatherList = new ArrayList<Weather>();
        String imageUri = "drawable://" + R.drawable.ic_launcher_background;
        weatherList.add(new Weather("א'", "14.5", "3:00", "25", "45", imageUri));

        WeatherAdapter weatheradapter = new WeatherAdapter(weatherList);
        recyclerView.setAdapter(weatheradapter);
        return root;
    }
}
