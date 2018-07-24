package com.example.stephen.jg;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MyRecyclerViewAdapter.ItemClickListener  {
    public MyRecyclerViewAdapter mAdapter;
    public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mThumbs;
    private List<String> mAuthors;
    private List<String> mTitles;
    private List<String> mUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rvPosters);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Initialize the adapter and attach it to the RecyclerView



        URL url = null;
        try {
            url = new URL("http://juggling.tv/search/jugglegram/date");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new fetch().execute(url);
    }

    @Override
    public void onItemClick(int position) {
        Intent toDetailActivity = new Intent(MainActivity.this, Detail.class);
        toDetailActivity.putExtra("author", mAuthors.get(position));
        toDetailActivity.putExtra("url", mUrls.get(position));
        toDetailActivity.putExtra("title", mTitles.get(position));
        toDetailActivity.putExtra("thumb", mThumbs.get(position));
        startActivity(toDetailActivity);
    }

    /////////////////////////////////////// START DATA FETCH TASK /////////////////////////////////
    class fetch extends AsyncTask<URL, Void, String>

    {
        // Do in background gets the json recipe data from internet
        @Override
        protected String doInBackground(URL... urls) {
            String fetchResults = null;
            try {
                fetchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Return the results to the onPostExecute method
            return fetchResults;
        }

        // On post execute task
        @Override
        protected void onPostExecute(String recipes) {
            // Iterate though the list of recipes and add them to the DB
            print_results(recipes);

        }
    }
    ////////////////////////////////////////// END DATA FETCH TASK /////////////////////////////////

    //*************************************** START PARSE TASK *************************************
    private void print_results(String text) {
        String text_copy = text;
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> thumbs = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        while (text.contains("videothumb")){
            text = text.substring(text.indexOf("videothumb"));
            String url = text.substring(text.indexOf("href=")+6,
                    text.indexOf("><")-1);
            Log.d("LOG", "asdf video source: " + url);
            text = text.substring(text.indexOf("videothumb")+123);
            urls.add(url);
        }
        text = text_copy;
        while (text.contains("videothumb")){
            text = text.substring(text.indexOf("videothumb"));
            String url = text.substring(text.indexOf("src=")+5,
                    text.indexOf(".jpg")+4);
            Log.d("LOG", "asdf thumb: " + url);
            text = text.substring(text.indexOf("videothumb")+123);
            thumbs.add(url);
        }
        text = text_copy;
        text = text.substring(text.indexOf("videothumb"));
        while (text.contains("videothumb")){
            text = text.substring(text.indexOf("target=\"_parent\"")+17);
            String author = text.substring(0, text.indexOf("/a")-1);
            Log.d("LOG", "asdf author" + author);
            text = text.substring(text.indexOf("videothumb")+10);
            authors.add(author);
        }
        text = text_copy;
        while (text.contains("videothumb")){
            text = text.substring(text.indexOf("videothumb"));
            String title = text.substring(text.indexOf("alt=")+5,
                    text.indexOf("/>")-1);
            Log.d("LOG", "asdf title: " + title);
            text = text.substring(text.indexOf("videothumb")+123);
            titles.add(title);
        }
        mThumbs = thumbs;
        mTitles = titles;
        mUrls = urls;
        mAuthors = authors;

        mAdapter = new MyRecyclerViewAdapter(this, thumbs, urls, authors, titles);
        // Start listening for clicks
        mAdapter.setClickListener(this);
        // Set adapter to mRecyclerView
        mRecyclerView.setAdapter(mAdapter);
    }
    //***************************************** END PARSE TASK *************************************
}