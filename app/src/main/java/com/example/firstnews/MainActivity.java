package com.example.firstnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final int LOCATION_PERMISSION_REQUEST = 1;
    final int SETTINGS_REQUEST = 2;
    final int PENDING_ID = 5;
    AlarmManager alarmManager;

    Menu tempMenu;
    MenuItem permission;

    //List<Weather> weatherList = new ArrayList<Weather>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(R.string.main_title);
        textView.setTextSize(30);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(getResources().getColor(R.color.colorRed));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textView);
        //Intent intent = new Intent(this, BackgroundNotificationService.class);
        NewsFragment newsFragment = NewsFragment.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NewsFragment.getNews();
            }
        }, 1000);


        WeatherFragment weatherFragment = WeatherFragment.getInstance(this);
        WeatherFragment.startLocationAndWeather();
        //SystemClock.sleep(500);


        getFragmentManager().beginTransaction().add(R.id.frame_container2, newsFragment, "news_fragment").commit();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSportTitle();
            }
        },1000);

        //final Intent intent = new Intent(MainActivity.this, NotificationService.class);
        //startService(intent);

        if(Build.VERSION.SDK_INT>=23){
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if(hasLocationPermission != getPackageManager().PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
            else {
                getFragmentManager().beginTransaction().add(R.id.frame_container1, weatherFragment, "weather_fragment").commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCity();
                    }
                },1000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCity();
                    }
                },1000);
            }
        }
        else {
            getFragmentManager().beginTransaction().add(R.id.frame_container1, weatherFragment, "weather_fragment").commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCity();
                }
            },1000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setCity();
                }
            },1000);
        }
    }

    public void setCity(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                TextView textView = findViewById(R.id.weather_title_tv);
                textView.setText("מזג האוויר ב"+sp.getString("city_weather", "מיקומך"));
            }
        });
    }

    public void setSportTitle(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                TextView textView = findViewById(R.id.news_title_tv);
                textView.setText(R.string.sport_title);
                //textView.setText(sp.getString("sport", "חדשות הספורט"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        tempMenu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.permission_location_settings){

            /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:"+getPackageName()));
            startActivity(intent);*/

            if(Build.VERSION.SDK_INT>=23){
                int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                if(hasLocationPermission != getPackageManager().PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        }
        else if(item.getItemId()==R.id.action_notifications){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.permission_title).setMessage(R.string.permission_msg)
                        .setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:"+getPackageName()));
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                permission = tempMenu.findItem(R.id.permission_location_settings);
                                permission.setVisible(true);

                                //getFragmentManager().beginTransaction().add(R.id.frame_container1, WeatherFragment.getInstance(this), "weather_fragment").commit();

                            }
                        }).setCancelable(false).show();
            }
            else {
                WeatherFragment weatherFragment = WeatherFragment.getInstance(this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WeatherFragment.startLocationAndWeather();
                    }
                },1000);
                getFragmentManager().beginTransaction().add(R.id.frame_container1, weatherFragment, "weather_fragment").commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setCity();

                    }
                },1000);
            }
        }
    }
}
