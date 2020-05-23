package com.example.firstnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int LOCATION_PERMISSION_REQUEST = 1;
    final int SETTINGS_REQUEST = 2;
    final int PENDING_ID = 5;
    AlarmManager alarmManager;
    SwitchPreference prefSwitch;

    //List<Weather> weatherList = new ArrayList<Weather>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.main_title);
        }
        //Intent intent = new Intent(this, BackgroundNotificationService.class);

        //prefSwitch =

        /*TextView textView = findViewById(R.id.main_title_tv);
        textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            callNotification();
                                        }
                                    });*/
                /*alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                int time=0;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                String choiceTime = sp.getString("notification_time", "2");
                boolean checked = sp.getBoolean("notification_active", false);

                if(checked) {
                    switch (choiceTime) {
                        case "0":
                            //60 sec
                            time = 60;
                            break;
                        case "1":
                            //30 min
                            time = 30 * 60;
                            break;
                        case "2":
                            //1 hour
                            time = 10;
                            break;
                    }

                    Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time * 1000, pendingIntent);
                    Toast.makeText(MainActivity.this, R.string.notification_active, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, R.string.notification_cancel, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, PENDING_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }

            }
        });*/


        //final Intent intent = new Intent(MainActivity.this, NotificationService.class);
        //startService(intent);

        if(Build.VERSION.SDK_INT>=23){
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if(hasLocationPermission != getPackageManager().PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            else {
                getFragmentManager().beginTransaction()
                        .add(R.id.frame_container1, new WeatherFragment(), "weather_fragment")
                        .add(R.id.frame_container2, new NewsFragment(), "news_fragment").commit();
            }
        }
        else {
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_container1, new WeatherFragment(), "weather_fragment").commit();
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_container2, new NewsFragment(), "news_fragment").commit();
        }
     }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_location_settings){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:"+getPackageName()));
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.action_notifications){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SETTINGS_REQUEST){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String choiceTime = sp.getString("notification_time", "0");
            String choiceKind = sp.getString("notification_kind", "0");
            int time=0;
            if(choiceKind.equals(R.string.last_news)) {
                switch (choiceTime) {
                    case "0":
                        //never
                        break;
                    case "1":
                        //60 sec
                        time = 6000;
                        break;
                    case "2":
                        //30 min
                        break;
                    case "3":
                        //1 hour
                        break;
                }
            }
            else{
                switch (choiceTime) {
                    case "0":
                        //never
                        break;
                    case "1":
                        //60 sec
                        break;
                    case "2":
                        //30 min
                        break;
                    case "3":
                        //1 hour
                        break;
                }
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.error).setMessage(R.string.permission_msg)
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
                                finish();
                            }
                        }).setCancelable(false).show();
            }
            else {
                getFragmentManager().beginTransaction()
                        .add(R.id.frame_container1, new WeatherFragment(), "weather_fragment").commit();
                getFragmentManager().beginTransaction()
                        .add(R.id.frame_container2, new NewsFragment(), "news_fragment").commit();
            }
        }
    }
}
