package com.example.oyeleke.renderer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.oyeleke.renderer.Models.Posts;
import com.example.oyeleke.renderer.R;

import java.util.List;

/**
 * Created by oyeleke on 2/25/18.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewholder>{
   private List<Posts>imagePosts;
    private Context mContext;

    public GalleryAdapter(List<Posts> imagePosts, Context mContext) {
        this.imagePosts = imagePosts;
        this.mContext = mContext;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail,parent,false);
        return new MyViewholder(v);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {
        Posts post = imagePosts.get(position);

        Glide.with(mContext)
                .load(post.getPost_body())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return imagePosts.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public MyViewholder(View itemView) {
            super(itemView);
            thumbnail = (ImageView)itemView.findViewById(R.id.iv_thumbnail);
        }
    }

    public interface ClickListener{
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        };
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
