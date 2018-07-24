package com.example.stephen.jg;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Detail extends AppCompatActivity {

    public String mUrl;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String title = getIntent().getExtras().getString("title");
        String author = getIntent().getExtras().getString("author");
        mUrl = getIntent().getExtras().getString("url");


        TextView titleTextView = findViewById(R.id.title_detail_text_view);
        TextView authorTextView = findViewById(R.id.author_detail_text_view);

        titleTextView.setText(title);
        authorTextView.setText(author);

        try {
            new fetch().execute(new URL(mUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //************************************** START EXOPLAYER TASKS *********************************
    /**
     * Initialize ExoPlayer.
     * and https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            MediaSource mediaSource = buildMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    // Review said: "release the Exoplayer in onPause or onStop"
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    // Release ExoPlayer
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
    //**************************************** END EXOPLAYER TASKS *********************************


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
        protected void onPostExecute(String content) {
            String movieId = mUrl;
            movieId = movieId.substring(10);
            movieId = movieId.substring(movieId.indexOf("/")+1);
            Log.d("LOG", "asdf movie id: "+movieId);
            String base = "http://juggling.tv/download/";
            String url = content.substring(content.indexOf("encoded/"+movieId));
            url = url.substring(0, url.indexOf(">")-1);

            //Play the url in the exoplayer
            mPlayerView = findViewById(R.id.playerView);
            // Load the question mark as the background image until the user answers the question.
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.question_mark));
            initializePlayer(Uri.parse(base+url+".mp4"));
            // Restore the Exoplayer's start/pause state
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    ////////////////////////////////////////// END DATA FETCH TASK /////////////////////////////////
}
