package com.example.kim.finalprojectowner1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kim.finalprojectowner1.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    List<Integer> lstImages;
    Context context;
    LayoutInflater inflater;

    public ViewPagerAdapter(List<Integer> lstImages, Context context) {
        this.lstImages = lstImages;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=inflater.inflate(R.layout.list_item1,container,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(lstImages.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return lstImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}