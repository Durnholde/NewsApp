package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    private final String URL = "http://content.guardianapis.com/search?api-key=bec20f1a-86b1-4b31-a002-e1260d55caba&show-tags=contributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            ((TextView) findViewById(R.id.empty_view)).setText(R.string.no_connection_info);
        }
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String question = preferences.getString(getString(R.string.settings_question_key), getString(R.string.settings_question_default));
        String order = preferences.getString(getString(R.string.settings_order_key), getString(R.string.settings_order_default));
        String section = preferences.getString(getString(R.string.settings_section_key), getString(R.string.settings_section_default));

        uriBuilder.appendQueryParameter("order-by", order);
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("q", question);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        loadData(news);
        ((TextView) findViewById(R.id.empty_view)).setText(R.string.no_data_info);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        loadData(new ArrayList<News>());
    }

    private void loadData(ArrayList<News> list) {
        ListView listView = findViewById(R.id.list);
        NewsAdapter adapter = new NewsAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty_view));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News current = (News) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(current.getUrl()));
                startActivity(intent);
            }
        });
    }


    /**
     * Menu methods.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
