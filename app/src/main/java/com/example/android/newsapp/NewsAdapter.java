package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent, false);

        News currentNews = getItem(position);

        TextView view = listItemView.findViewById(R.id.news_title);
        view.setText(currentNews.getTitle());
        view = listItemView.findViewById(R.id.news_section);
        view.setText(currentNews.getSection());

        view = listItemView.findViewById(R.id.news_author);
        String author = currentNews.getAuthor();
        if (author != null && author.length() > 0) {
            view.setText(author);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }

        String date = currentNews.getDate();
        if (date != null && date.length() > 0) {
            view = listItemView.findViewById(R.id.news_date);
            view.setVisibility(View.VISIBLE);
            view.setText(date.substring(0, 10));
            view = listItemView.findViewById(R.id.news_time);
            view.setVisibility(View.VISIBLE);
            view.setText(date.substring(11, 19));
        } else {
            view = listItemView.findViewById(R.id.news_date);
            view.setVisibility(View.GONE);
            view = listItemView.findViewById(R.id.news_time);
            view.setVisibility(View.GONE);
        }
        return listItemView;
    }
}
