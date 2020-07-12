package com.example.appmusicbotnav.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.modelOnline.Baihat;

import java.util.ArrayList;

public class BXHAdapter extends ArrayAdapter<Baihat> {
    Context context;
    ArrayList<Baihat> bh ;
    PopupMenu popupMenu;
    Menu menu;
    public BXHAdapter(@NonNull Context context, ArrayList<Baihat> bh) {
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
    public Baihat getItem(int position) {
        return bh.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_bhbxh, parent, false);
        }
        Baihat currentSong = bh.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_music_namebxh);
        TextView tvSubtitle = listItem.findViewById(R.id.tv_music_subtitlebxh);
        tvTitle.setTextColor(Color.WHITE);
        tvSubtitle.setTextColor(Color.WHITE);
        ImageView imv = listItem.findViewById(R.id.img_menubxh);
        taomenu(imv, currentSong);
        tvTitle.setText(currentSong.getTenBaiHat());
        tvSubtitle.setText(currentSong.getTenTacGia());
        return listItem;
    }
    public void taomenu(ImageView iv, Baihat bh)
    {
        popupMenu = new PopupMenu(getContext(), iv);
        menu = popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(R.menu.menu_bxh_kbeat, menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.download_kbeat:
                        break;

                    case R.id.themvaoplaylist_kbeat:
                        break;

                }
                return false;
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }
}

