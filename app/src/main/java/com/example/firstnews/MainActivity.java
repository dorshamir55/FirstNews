package com.example.firstnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager weatherPager = findViewById(R.id.weather_pager);
        DayAdapter adapter = new DayAdapter(getSupportFragmentManager());
        weatherPager.setAdapter(adapter);


    }

    private class DayAdapter extends FragmentStatePagerAdapter {

        public DayAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return Day.values().length;
        }
    }
}
