package com.example.oyeleke.renderer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.oyeleke.renderer.Models.Posts;
import com.example.oyeleke.renderer.R;
import com.example.oyeleke.renderer.adapters.GalleryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private List<Posts>postsList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private RecyclerView mImagesRecyclerView;
    private GalleryAdapter galleryAdapter;
    private ProgressBar mProgressBar;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mImagesRecyclerView = (RecyclerView)findViewById(R.id.rv_image);
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        frameLayout = (FrameLayout)findViewById(R.id.fl_images_container); 
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageActivity.this,AddPictureActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        galleryAdapter = new GalleryAdapter(postsList,getApplicationContext());
        mImagesRecyclerView.setLayoutManager(layoutManager);
        mImagesRecyclerView.setAdapter(galleryAdapter);

        mImagesRecyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), mImagesRecyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("posts", (Serializable) postsList);
                bundle.putInt("position", position);

                FragmentTransaction ft  = getFragmentManager().beginTransaction();
                SlideShowDialogFragment newFragment = SlideShowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "show");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getPostFromDatabase();


    }

    private void getPostFromDatabase(){

        mProgressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    postsList.clear();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Posts post = postSnapshot.getValue(Posts.class);
                        postsList.add(post);
                    }
                    galleryAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    




}
