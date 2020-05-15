package com.example.firstnews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class WeatherFragment extends android.app.Fragment {

    /*public static WeatherFragment newInstance (int num){
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("day", num);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }*/

    private static List<Weather> weatherList;
    Context context;

    static WeatherFragment newInstance(List<Weather> i_weatherList){
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
        final RecyclerView recyclerView = root.findViewById(R.id.weather_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        WeatherViewModel viewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(WeatherViewModel.class);
        viewModel.getWeatherList().observe((LifecycleOwner) getActivity(), new Observer<List<Weather>>() {
            @Override
            public void onChanged(List<Weather> weatherList) {
                WeatherAdapter adapter = new WeatherAdapter(context, weatherList);
                recyclerView.setAdapter(adapter);
            }
        });

        //weatherList = new ArrayList<Weather>();
        //String imageUri = "drawable://" + R.drawable.ic_launcher_background;
        //weatherList.add(new Weather("א'", "14.5", "3:00", 25.0, 45.0, imageUri));

        //WeatherAdapter weatheradapter = new WeatherAdapter(weatherList);
        //recyclerView.setAdapter(weatheradapter);
        return root;
    }
}
