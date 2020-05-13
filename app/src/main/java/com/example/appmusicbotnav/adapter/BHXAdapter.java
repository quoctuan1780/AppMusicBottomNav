package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;

public class BHXAdapter extends ArrayAdapter<BaiHat> {
    Context context;
    ArrayList<BaiHat> bh = new ArrayList<>();
    public BHXAdapter(@NonNull Context context, ArrayList<BaiHat> bh) {
        super(context, 0, bh);
        this.context = context;
        this.bh =bh;
    }
    @Override
    public int getCount() {
        return bh.size();
    }

    @Nullable
    @Override
    public BaiHat getItem(int position) {
        return bh.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_baihat, parent, false);
        }
        BaiHat currentSong = bh.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_music_name);
        TextView tvSubtitle = listItem.findViewById(R.id.tv_music_subtitle);
        tvTitle.setText(currentSong.getTitle());
        tvSubtitle.setText(currentSong.getSubTitle());
        return listItem;
    }
}
