package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.model.Slide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuangCaoOnlineAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Slide> slideArrayList;

    public QuangCaoOnlineAdapter(Context context, ArrayList<Slide> slideArrayList) {
        this.context = context;
        this.slideArrayList = slideArrayList;
    }

    @Override
    public int getCount() {
        return slideArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_quangcao, null);
        ImageView iv_quangcao = view.findViewById(R.id.iv_quangcao);
        Picasso.with(context).load(slideArrayList.get(position).getHinh()).into(iv_quangcao);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
