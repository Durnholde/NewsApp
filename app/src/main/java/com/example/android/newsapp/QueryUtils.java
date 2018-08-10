package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static String makeHttpRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        String jsonResponse = "";

        if (url == null)
            return jsonResponse;

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Connection to url could not be established. Response from the url: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results from url.", e);
        } finally {
            if (connection != null)
                connection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String extracted = bufferedReader.readLine();
            while (extracted != null) {
                builder.append(extracted);
                extracted = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }

    public static ArrayList<News> extractDataFromJson(String json) {
        ArrayList<News> list = new ArrayList<News>();

        try {
            JSONObject root = new JSONObject(json);
            JSONArray array = root.getJSONObject("response").getJSONArray("results");
            if (array.length() > 0) {
                String title;
                String section;
                String author;
                String date;
                String url;
                for (int i = 0; i < array.length(); i++) {
                    title = array.getJSONObject(i).optString("webTitle");
                    section = array.getJSONObject(i).optString("sectionName");
                    date = array.getJSONObject(i).optString("webPublicationDate");
                    url = array.getJSONObject(i).optString("webUrl");

                    JSONArray authorArray = array.getJSONObject(i).getJSONArray("tags");
                    if (authorArray.length() > 0) {
                        author = authorArray.optJSONObject(0).optString("webTitle");
                        for (int j = 1; j < authorArray.length(); j++) {
                            author += ", " + authorArray.getJSONObject(j).optString("webTitle");
                        }
                    }
                    else{
                        author = "";
                    }
                    list.add(new News(title, section, author, date, url));
                }
            } else
                return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing data from JSON", e);
        }

        return list;
    }
}
