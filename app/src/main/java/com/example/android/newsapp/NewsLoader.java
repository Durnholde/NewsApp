package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    private static String mURL;
    private static final String LOG_TAG = NewsLoader.class.getName();

    public NewsLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    public ArrayList<News> loadInBackground() {
        String jsonResponse = "";

        try {
            jsonResponse = QueryUtils.makeHttpRequest(mURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error loading json from url.", e);
        }

        return QueryUtils.extractDataFromJson(jsonResponse);
    }
}
