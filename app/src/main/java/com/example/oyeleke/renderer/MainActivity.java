package com.example.oyeleke.renderer;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int VID_REQUEST = 2;
    private  Uri path;
    public static final String  KEY_VIDEO_URL = "url";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToImages(View view){
        startActivity(new Intent(MainActivity.this,ImageActivity.class));
    }

    public void  goToVideos(View view){
        selectVideo();
    }


    private void selectVideo(){
        Intent intent  = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,VID_REQUEST);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == VID_REQUEST && resultCode == RESULT_OK && data != null){
            path = data.getData();

            Log.d("video_path", String.valueOf(path));

            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            intent.putExtra(KEY_VIDEO_URL,String.valueOf(path));
            startActivity(intent);


        }

    }
}
