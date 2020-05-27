package com.example.firstnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static com.example.firstnews.WeatherFragment.context;

public class MainActivity extends AppCompatActivity {

    final int LOCATION_PERMISSION_REQUEST = 1;
    final int SETTINGS_REQUEST = 2;
    final int PENDING_ID = 5;
    final String NEWS_FRAGMENT = "news_fragment";
    final String WEATHER_FRAGMENT = "weather_fragment";
    AlarmManager alarmManager;


    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;


    //Menu tempMenu;
    //MenuItem permission;

    //List<Weather> weatherList = new ArrayList<Weather>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        toolbar = findViewById(R.id.main_toolbar);
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


        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_container2, new NewsFragment(), NEWS_FRAGMENT)
                .add(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();

        setSportTitle();
        if (Build.VERSION.SDK_INT >= 23) {
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermission != getPackageManager().PERMISSION_GRANTED) {
                setNotAvailable();
            } else {
                setCity();
            }
        }
    }


    public void setCity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                TextView textView = findViewById(R.id.weather_title_tv);
                textView.setText("מזג האוויר ב" + sp.getString("city_weather", "מיקומך"));
            }
        }, 1000);
    }

    public void setNotAvailable() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView textView = findViewById(R.id.weather_title_tv);
                textView.setText(R.string.weather_not_available);
            }
        }, 1000);
    }

    public void setSportTitle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                TextView textView = findViewById(R.id.news_title_tv);
                textView.setText(R.string.sport_title);
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        WeatherFragment.tempMenu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.permission_location_settings) {

            /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:"+getPackageName()));
            startActivity(intent);*/

            if (Build.VERSION.SDK_INT >= 23) {
                int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasLocationPermission != getPackageManager().PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        } else if (item.getItemId() == R.id.icon_refresh) {
            Fragment weatherfragment = getSupportFragmentManager().findFragmentByTag(WEATHER_FRAGMENT);
            Fragment newsfragment = getSupportFragmentManager().findFragmentByTag(NEWS_FRAGMENT);

            if (newsfragment == null) {
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.frame_container2, new NewsFragment(), NEWS_FRAGMENT).commit();
                setSportTitle();
            } else {
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_container2, new NewsFragment(), NEWS_FRAGMENT).commit();
                setSportTitle();
            }

            if (Build.VERSION.SDK_INT >= 23) {
                int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasLocationPermission == getPackageManager().PERMISSION_GRANTED) {
                    if (weatherfragment == null) {
                        fragmentManager = getSupportFragmentManager();
                        transaction = fragmentManager.beginTransaction();
                        transaction.add(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();
                        WeatherFragment.startLocation();
                        setCity();
                    } else {
                        fragmentManager = getSupportFragmentManager();
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();
                        //WeatherFragment.startLocation();
                        setCity();
                    }
                }
            }

        } else if (item.getItemId() == R.id.action_notifications) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title).setMessage(R.string.permission_msg)
                        .setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                WeatherFragment.permission = WeatherFragment.tempMenu.findItem(R.id.permission_location_settings);
                                WeatherFragment.permission.setVisible(true);

                                //getFragmentManager().beginTransaction().add(R.id.frame_container1, WeatherFragment.getInstance(this), "weather_fragment").commit();

                            }
                        }).setCancelable(false).show();
            } else {
                WeatherFragment.permission = WeatherFragment.tempMenu.findItem(R.id.permission_location_settings);
                WeatherFragment.permission.setVisible(false);
                if (Build.VERSION.SDK_INT >= 23) {
                    Fragment weatherfragment = getSupportFragmentManager().findFragmentByTag(WEATHER_FRAGMENT);
                    int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                    if (hasLocationPermission == getPackageManager().PERMISSION_GRANTED) {
                        if (weatherfragment == null) {
                            fragmentManager = getSupportFragmentManager();
                            transaction = fragmentManager.beginTransaction();
                            transaction.add(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();
                            WeatherFragment.startLocation();
                            setCity();
                        } else {
                            fragmentManager = getSupportFragmentManager();
                            transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();
                            //WeatherFragment.startLocation();
                            setCity();
                        }

                    }
                }
            }
        }
    }

}



        /*private void weatherFunction() {
            Log.d("tag", "weatherFuncation");

            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            Log.d("tag", "1");
            LocationCallback callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Log.d("tag", "Before super method");
                    super.onLocationResult(locationResult);
                    Log.d("tag", "onLocationResult");
                    Location lastLocation = locationResult.getLastLocation();
                    Log.d("tag", "lastLocation");
                    SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("latitude", String.valueOf(lastLocation.getLatitude()));
                    editor.putString("longitude", String.valueOf(lastLocation.getLongitude()));
                    editor.apply();
                    Log.d("tag", "Before FragmentManager");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();
                }
            };
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            client.requestLocationUpdates(request, callback, null);

        }

        private void weatherFunctionReplace(){
            Log.d("tag", "weatherFuncationReplace");

            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            Log.d("tag", "1");
            LocationCallback callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Log.d("tag", "Before super method");
                    super.onLocationResult(locationResult);
                    Log.d("tag", "onLocationResult");
                    Location lastLocation = locationResult.getLastLocation();
                    Log.d("tag", "lastLocation");
                    SharedPreferences sp = android.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("latitude", String.valueOf(lastLocation.getLatitude()));
                    editor.putString("longitude", String.valueOf(lastLocation.getLongitude()));
                    editor.apply();
                    Log.d("tag", "Before FragmentManager");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container1, new WeatherFragment(), WEATHER_FRAGMENT).commit();
                }
            };
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            client.requestLocationUpdates(request, callback, null);

        }
    }
}*/
