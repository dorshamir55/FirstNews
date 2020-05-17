package com.example.firstnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


    public class NewsAdapter extends RecyclerView.Adapter<com.example.firstnews.NewsAdapter.NewsViewHolder> {
        private List<News> newsList;

        public NewsAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        static class NewsViewHolder extends RecyclerView.ViewHolder{
            TextView titleTv;
            TextView descriptionTv;
            ImageView imageIv;
            TextView dateTv;

            NewsViewHolder(View v){
                super(v);
                titleTv = (TextView)v.findViewById(R.id.news_title_tv);
                descriptionTv = (TextView)v.findViewById(R.id.news_description_tv);
                imageIv = (ImageView)v.findViewById(R.id.news_image_iv);
                dateTv = (TextView) v.findViewById(R.id.news_date_tv);
            }
        }

        @NonNull
        @Override
        public com.example.firstnews.NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cell, parent, false);
            return new com.example.firstnews.NewsAdapter.NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.firstnews.NewsAdapter.NewsViewHolder holder, int position) {
            News news = newsList.get(position);
            holder.titleTv.setText(news.getTitle());
            Picasso.get().load(news.getImage()).resize(400,275).into(holder.imageIv);
            holder.descriptionTv.setText(news.getDescription());
            holder.dateTv.setText(news.getDate());
        }

        @Override
        public int getItemCount() {
            if(newsList!=null)
                return newsList.size();
            else
                return 0;
        }
}
