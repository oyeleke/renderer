package com.example.oyeleke.renderer.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
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

//  adapter
public class MyViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;

    private List<Posts>postsList;
    private Context mContext;

    public MyViewPagerAdapter(List<Posts> postsList, Context mContext) {
        this.postsList = postsList;
        this.mContext = mContext;
    }

    public MyViewPagerAdapter() {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

        ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

        Posts posts = postsList.get(position);

        Glide.with(mContext).load(posts.getPost_body())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewPreview);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return postsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
