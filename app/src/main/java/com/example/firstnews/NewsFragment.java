package com.example.firstnews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends android.app.Fragment {
    private List<News> newsList;
    NewsAdapter newsAdapter;

    Context context;
    final String BASE_LINK = "http://newsapi.org/v2/top-headlines?country=il&category=sports&apiKey=77d0acf9be214ed4b7c4c438e081d389";

    TextView sportTv;

    public NewsFragment(){
        newsList = new ArrayList<>();
        this.context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getNews();
        View root = inflater.inflate(R.layout.news_fragment, container, false);
        //SystemClock.sleep(800);
        final RecyclerView recyclerView = root.findViewById(R.id.news_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        newsAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(newsAdapter);

        return root;
    }

    private void getNews() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_LINK +"", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject rootObject = new JSONObject(response);
                    //JSONObject listObject = response.getJSONObject("articles");
                    sportTv = getView().findViewById(R.id.news_title_tv);
                    sportTv.setText(R.string.sport_title);
                    JSONArray articlesArray = response.getJSONArray("articles");
                    int i;
                    for(i=0; i<articlesArray.length();i++){
                        JSONObject currentElementObject = articlesArray.getJSONObject(i);
                        String title = currentElementObject.getString("title");

                        String date = currentElementObject.getString("publishedAt");
                        String part1 = date.substring(11,19);
                        String part2 = date.substring(0,10);
                        date = part1+"  "+part2;
                        String description = currentElementObject.getString("description");
                        String icon = currentElementObject.getString("urlToImage");

                        News news = new News(title, description, icon, date);
                        newsList.add(news);
                        //weatheradapter.notifyItemInserted(i);
                    }
                    newsAdapter.notifyItemInserted(i-1);

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
}
