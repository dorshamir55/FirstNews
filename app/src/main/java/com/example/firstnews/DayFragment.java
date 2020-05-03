package com.example.firstnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DayFragment extends Fragment {

    public static DayFragment newInstance (int num){
        DayFragment dayFragment = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("day", num);
        dayFragment.setArguments(bundle);
        return dayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.day_fragment, container, false);
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
        }
        return root;
    }
}
