package com.example.oyeleke.renderer;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private int currentPosition = -1;
    private boolean isPlaying = false;

    private static final String KEY_DURATION = "duration";
    private static final String KEY_IS_PLAYING = "isPlaying";
    public static final String  KEY_VIDEO_URL = "url";
    private static final String LOG_TAG = "tag";

    private MediaController mediaController;

    private VideoView mVideoView;
    private ProgressBar progressBar;
    private View overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        mVideoView = (VideoView)findViewById(R.id.video_view);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        overlay = findViewById(R.id.overlay);



        try {



            String videoUrl = getIntent().getStringExtra(KEY_VIDEO_URL);

            Log.d("video_url", videoUrl);

            Uri videoUri = Uri.parse(videoUrl);
            Log.d("videoUri", "i suspect this one");

            mediaController = new MediaController(this);
            mediaController.setAnchorView(findViewById(R.id.frame));
            mVideoView.setMediaController(mediaController);
            mVideoView.setOnCompletionListener(this);
            mVideoView.setOnPreparedListener(this);
            mVideoView.setVideoURI(videoUri);

            Log.d("Welcome","Video Url might be parsed");


        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

        if (savedInstanceState != null) {

            currentPosition = savedInstanceState.getInt(KEY_DURATION);
            mVideoView.seekTo(currentPosition + 10);
            isPlaying = savedInstanceState.getBoolean(KEY_IS_PLAYING);

            if (!isPlaying)
                mVideoView.pause();

        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_DURATION, currentPosition);
        outState.putBoolean(KEY_IS_PLAYING, isPlaying);

    }

    @Override
    protected void onPause() {

        super.onPause();
        currentPosition = mVideoView.getCurrentPosition();
        isPlaying = mVideoView.isPlaying();

    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        hideViews(progressBar, overlay);

        mVideoView.start();

        Log.e(LOG_TAG, "onPrepared");
    }

    @Override
    public void onBackPressed() {

        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();

        }

        super.onBackPressed();

    }

    public static void hideViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }

    }

    public static void showViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }

    }
}
