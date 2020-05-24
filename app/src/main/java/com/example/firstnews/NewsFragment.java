package com.example.firstnews;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

@SuppressLint("ValidFragment")
public class NewsFragment extends android.app.Fragment {
    public static NewsFragment instance;
    private static Context context;
    static private List<News> newsList;
    static NewsAdapter newsAdapter;
    static News lastNews;

    static final String BASE_LINK = "http://newsapi.org/v2/top-headlines?country=il&category=sports&apiKey=77d0acf9be214ed4b7c4c438e081d389";
    //final String DEFAULT_ICON="https://cdn3.iconfinder.com/data/icons/iconano-text-editor/512/005-X-512.png";
    //final int NOTIFICATION_ID = 3;
    static TextView sportTv;

    public NewsFragment(Context context){
        newsList = new ArrayList<>();
        this.context=context;
    }

    public static NewsFragment getInstance(Context context){
        if(instance==null){
            instance = new NewsFragment(context);
        }
        return  instance;
    }

    //getFragmentManager().beginTransaction().add(R.id.frame_container1, new WeatherFragment(), "weather_fragment").commit();
    //getFragmentManager().beginTransaction().add(R.id.frame_container2, new NewsFragment(), "news_fragment").commit();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getNews();
        //SystemClock.sleep(500);
        View root = inflater.inflate(R.layout.news_fragment, container, false);
        //SystemClock.sleep(800);
        final RecyclerView recyclerView = root.findViewById(R.id.news_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        newsAdapter = new NewsAdapter(newsList);

        newsAdapter.setListener(new NewsAdapter.MyNewsListener() {
            @Override
            public void onNewsClicked(int position, View view, String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        recyclerView.setAdapter(newsAdapter);

        return root;
    }

    public static void getLastNews(){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK +"", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject rootObject = new JSONObject(response);
                    //JSONObject listObject = response.getJSONObject("articles");

                    //sportTv = sportTv.findViewById(R.id.news_title_tv);
                    //sportTv.setText(R.string.sport_title);

                    JSONArray articlesArray = response.getJSONArray("articles");
                    int i=0;
                    JSONObject currentElementObject = articlesArray.getJSONObject(i);
                    while(currentElementObject.getJSONObject("source").getString("name").equals("Israelhayom.co.il")){
                        i++;
                        currentElementObject = articlesArray.getJSONObject(i);
                    }
                    String title = currentElementObject.getString("title");

                    String date = currentElementObject.getString("publishedAt");
                    String part1 = date.substring(11,16);
                    String part2 = date.substring(0,4)+"."+date.substring(5,7)+"."+date.substring(8,10);
                    if(date.substring(5,6).equals("0")){
                        part2 = date.substring(8,10)+"."+date.substring(6,7)+"."+date.substring(0,4);
                    }
                    if(part1.substring(0,1).equals("0")){
                        part1 = date.substring(12,16);
                    }
                    date = part1+"  "+part2;
                    String description = currentElementObject.getString("description");
                    if(description.equals("null")){
                        description="";
                    }

                    String icon = currentElementObject.getString("urlToImage");
                        /*if(icon.equals("null")){
                            icon = DEFAULT_ICON;
                        }*/

                    String webUrl = currentElementObject.getString("url");

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor prefEditor = sp.edit();
                    prefEditor.putString("title_news", title);
                    prefEditor.putString("description_news", description);
                    prefEditor.putString("icon_news", icon);
                    prefEditor.putString("date_news", date);
                    prefEditor.putString("wenUrl_news", webUrl);
                    prefEditor.commit();

                    //lastNews = new News(title, description, icon, date, webUrl);

                    //final News lastNews = newsList.get(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();
    }

    public static void getNews() {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK +"", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject rootObject = new JSONObject(response);
                    //JSONObject listObject = response.getJSONObject("articles");

                    //sportTv = sportTv.findViewById(R.id.news_title_tv);
                    //sportTv.setText(R.string.sport_title);

                    JSONArray articlesArray = response.getJSONArray("articles");
                    int i;
                    for(i=0; i<articlesArray.length();i++){
                        JSONObject currentElementObject = articlesArray.getJSONObject(i);
                        while(currentElementObject.getJSONObject("source").getString("name").equals("Israelhayom.co.il")){
                            i++;
                            currentElementObject = articlesArray.getJSONObject(i);
                        }
                        String title = currentElementObject.getString("title");

                        String date = currentElementObject.getString("publishedAt");
                        String part1 = date.substring(11,16);
                        String part2 = date.substring(0,4)+"."+date.substring(5,7)+"."+date.substring(8,10);
                        if(date.substring(5,6).equals("0")){
                            part2 = date.substring(8,10)+"."+date.substring(6,7)+"."+date.substring(0,4);
                        }
                        if(part1.substring(0,1).equals("0")){
                            part1 = date.substring(12,16);
                        }
                        date = part1+"  "+part2;
                        String description = currentElementObject.getString("description");
                        if(description.equals("null")){
                            description="";
                        }

                        String icon = currentElementObject.getString("urlToImage");
                        /*if(icon.equals("null")){
                            icon = DEFAULT_ICON;
                        }*/

                        String webUrl = currentElementObject.getString("url");

                        News news = new News(title, description, icon, date, webUrl);
                        newsList.add(news);
                        //weatheradapter.notifyItemInserted(i);
                    }

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor prefEditor = sp.edit();
                    String sport = String.valueOf(R.string.sport_title);
                    prefEditor.putString("city_news", sport);
                    prefEditor.commit();

                    newsAdapter.notifyItemInserted(i-1);

                    lastNews = newsList.get(0);

                    //final News lastNews = newsList.get(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();
    }

    public static News getLastNew(){
        return lastNews;
    }
}
