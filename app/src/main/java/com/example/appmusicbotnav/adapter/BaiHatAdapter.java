package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;

public class BaiHatAdapter extends ArrayAdapter<BaiHat> implements Filterable {

    private Context mContext;
    private ArrayList<BaiHat> songList = new ArrayList<>();

    public BaiHatAdapter(Context mContext, ArrayList<BaiHat> songList) {
        super(mContext, 0, songList);
        this.mContext = mContext;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Nullable
    @Override
    public BaiHat getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_baihat, parent, false);
        }
        BaiHat currentSong = songList.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_music_name);
        TextView tvSubtitle = listItem.findViewById(R.id.tv_music_subtitle);
        tvTitle.setText(currentSong.getTitle());
        tvSubtitle.setText(currentSong.getSubTitle());
        return listItem;
    }
}
