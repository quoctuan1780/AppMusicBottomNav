package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.modelOnline.Baihat;
import java.util.ArrayList;

public class BaiHatOnlineAdapter extends ArrayAdapter<Baihat> {
    private Context mContext;
    private ArrayList<Baihat> songList;

    public BaiHatOnlineAdapter(Context mContext, ArrayList<Baihat> songList) {
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
    public Baihat getItem(int position) {
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
        Baihat currentSong = songList.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_music_name);
        TextView tvSubtitle = listItem.findViewById(R.id.tv_music_subtitle);
        tvSubtitle.setTextColor(Color.WHITE);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setText(currentSong.getTenBaiHat());
        tvSubtitle.setText(currentSong.getTenTacGia());
        return listItem;
    }

    public void updateBaihat(ArrayList<Baihat> baihatArrayList){
        this.songList = baihatArrayList;
        notifyDataSetChanged();
    }
}