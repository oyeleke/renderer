package com.example.oyeleke.renderer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oyeleke.renderer.Helper.Constants;
import com.example.oyeleke.renderer.Models.Posts;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AddPictureActivity extends AppCompatActivity {

    private ImageView mSelectPictureView, mImageConatiner;
    private Button mPostPictureButton;
    private DatabaseReference databaseReference;
    private Bitmap bitmap,mBitmap;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private final int IMG_REQUEST = 1;
    private Uri url;
    private String imageUrl;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        mPostPictureButton = (Button) findViewById(R.id.bt_post_picture_button);
        mSelectPictureView = (ImageView) findViewById(R.id.iv_selectPictureButton);
        mImageConatiner = (ImageView) findViewById(R.id.iv_image_view_conatiner);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        mSelectPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mPostPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                mImageConatiner.setDrawingCacheEnabled(true);
                mImageConatiner.buildDrawingCache();
                mBitmap = mImageConatiner.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                mImageConatiner.setDrawingCacheEnabled(false);
                byte[] data = baos.toByteArray();
                Log.d("picture..","picture processing done");


                String path = "pictures/" + UUID.randomUUID()+ ".png";
                Log.d("picture..",path);

                StorageReference pictureRatingsRef = storage.getReference(path);

                mPostPictureButton.setEnabled(false);
                mSelectPictureView.setEnabled(false);
                Log.d("picture..","upload_Started");

                final UploadTask uploadTask = pictureRatingsRef.putBytes(data);

                uploadTask.addOnSuccessListener(AddPictureActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("picture..","what Happened");
                        mPostPictureButton.setEnabled(true);
                        mSelectPictureView.setEnabled(true);


                        //noinspection VisibleForTests
                        url = taskSnapshot.getDownloadUrl();
                        if (url != null) {
                            imageUrl = url.toString();
                        }
                        Log.d("picture..","push_to_db_Started");

                        String id = databaseReference.push().getKey();


                        Posts mpost = new Posts(imageUrl, Constants.isAPicturePost);

                        databaseReference.child(id).setValue(mpost);
                        Log.d("picture..","push_to_db_ended");

                        Toast.makeText(AddPictureActivity.this, "Post added Successfully", Toast.LENGTH_LONG).show();

                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(AddPictureActivity.this, ImageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();





                    }
                });
            }
        });

    }






    private void selectImage(){
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                mImageConatiner.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
