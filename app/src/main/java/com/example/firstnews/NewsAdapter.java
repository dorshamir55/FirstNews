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
        private MyNewsListener listener;

        interface MyNewsListener {
            void onNewsClicked(int position, View view, String url);
        }

        public void setListener(MyNewsListener listener) {
            this.listener=listener;
        }

        public NewsAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        public class NewsViewHolder extends RecyclerView.ViewHolder{
            TextView titleTv;
            TextView descriptionTv;
            ImageView imageIv;
            TextView dateTv;
            String url;

            NewsViewHolder(View itemView){
                super(itemView);
                titleTv = (TextView)itemView.findViewById(R.id.news_title_tv);
                descriptionTv = (TextView)itemView.findViewById(R.id.news_description_tv);
                imageIv = (ImageView)itemView.findViewById(R.id.news_image_iv);
                dateTv = (TextView) itemView.findViewById(R.id.news_date_tv);


            }
        }

        @NonNull
        @Override
        public com.example.firstnews.NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_cell, parent, false);
            return new com.example.firstnews.NewsAdapter.NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final com.example.firstnews.NewsAdapter.NewsViewHolder holder, final int position) {
            News news = newsList.get(position);
            holder.titleTv.setText(news.getTitle());
            Picasso.get().load(news.getImage()).resize(400,275).into(holder.imageIv);
            holder.descriptionTv.setText(news.getDescription());
            holder.dateTv.setText(news.getDate());
            holder.url = news.getWebUrl();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onNewsClicked(position, v, holder.url);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(newsList!=null)
                return newsList.size();
            else
                return 0;
        }
}
