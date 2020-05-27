package com.example.firstnews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList;
    private final Context context;
    private MyNewsListener listener;

    interface MyNewsListener {
        void onNewsClicked(int position, View view);
    }

    public void setListener(MyNewsListener listener) {
        this.listener=listener;
    }

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        TextView descriptionTv;
        ImageView imageIv;
        TextView dateTv;
        ViewHolder(View itemView){
            super(itemView);
            titleTv = itemView.findViewById(R.id.news_title_tv);
            descriptionTv = itemView.findViewById(R.id.news_description_tv);
            imageIv = itemView.findViewById(R.id.news_image_iv);
            dateTv = itemView.findViewById(R.id.news_date_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) {
                        Log.d("tag", "holder.itemView clicked");
                        listener.onNewsClicked(getAdapterPosition(), v);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.titleTv.setText(news.getTitle());
        Picasso.get().load(news.getImage()).resize(400,275).into(holder.imageIv);
        holder.descriptionTv.setText(news.getDescription());
        holder.dateTv.setText(news.getDate());

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}