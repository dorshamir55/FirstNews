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
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
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
import static com.example.firstnews.WeatherFragment.context;
import static com.example.firstnews.WeatherFragment.weatherAdapter;

public class NewsFragment extends Fragment {
    private List<News> newsList;
    NewsAdapter newsAdapter;

    final String BASE_LINK = "https://newsapi.org/v2/top-headlines?country=il&category=sports&apiKey=77d0acf9be214ed4b7c4c438e081d389";
    final String DEFAULT_IMAGE ="https://aok.pte.hu/docs/felvi/image/sport-1.png";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d("tag", "called onCreateView - News");
        View root = inflater.inflate(R.layout.news_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.news_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        newsList =new ArrayList<>();
        newsAdapter = new NewsAdapter(getActivity(), newsList);
        recyclerView.setAdapter(newsAdapter);

        newsAdapter.setListener(new NewsAdapter.MyNewsListener() {
            @Override
            public void onNewsClicked(int position, View view){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsList.get(position).getWebUrl()));
                startActivity(browserIntent);
            }
        });
        recyclerView.setAdapter(newsAdapter);

        getNews();

        return root;
    }

    public void getNews() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articlesArray = response.getJSONArray("articles");
                            int i;
                            for(i=0; i<articlesArray.length();i++){

                                JSONObject currentElementObject = articlesArray.getJSONObject(i);
                                /*while(currentElementObject.getJSONObject("source").getString("name").equals("Israelhayom.co.il")){
                                    i++;
                                    currentElementObject = articlesArray.getJSONObject(i);
                                }
                                if(i<articlesArray.length()) {*/

                                News news = new News();
                                news.setTitle(currentElementObject.getString("title"));
                                news.setWebUrl(currentElementObject.getString("url"));
                                String urlToImage = (currentElementObject.getString("urlToImage"));
                                String description = (currentElementObject.getString("description"));
                                news.setDescription(description);
                                if(description.equals("null")){
                                    news.setDescription("");
                                }
                                news.setImage(urlToImage);
                                if(news.getImage().equals("null")){
                                    news.setImage(DEFAULT_IMAGE);
                                }

                                //   String title = currentElementObject.getString("title");
                                String date = currentElementObject.getString("publishedAt");
                                String part1 = date.substring(11, 16);
                                String part2 = date.substring(0, 4) + "." + date.substring(5, 7) + "." + date.substring(8, 10);
                                if (date.substring(5, 6).equals("0")) {
                                    part2 = date.substring(8, 10) + "." + date.substring(6, 7) + "." + date.substring(0, 4);
                                }
                                if (part1.substring(0, 1).equals("0")) {
                                    part1 = date.substring(12, 16);
                                }
                                date = part1 + "  " + part2;
                                news.setDate(date);
//                                String description = currentElementObject.getString("description");
//                                if(description.equals("null")){
//                                    description="";
//                                }

//                                String icon = currentElementObject.getString("urlToImage");
                        /*if(icon.equals("null")){
                            icon = DEFAULT_ICON;
                        }*/

//                                    String webUrl = currentElementObject.getString("url");

//                                News news = new News(title, description, icon, date, webUrl);
                                newsList.add(news);
                                //newsAdapter.notifyItemInserted(i);
                            }
                            //}

//                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//                            SharedPreferences.Editor prefEditor = sp.edit();
//                            String sport = String.valueOf(R.string.sport_title);
//                            prefEditor.putString("city_news", sport);
//                            prefEditor.commit();

//                            lastNews = newsList.get(0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        newsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

//    public static News getLastNew(){
//        return lastNews;
//    }
}
