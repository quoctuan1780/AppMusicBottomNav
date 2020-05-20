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
import com.example.appmusicbotnav.model.BaiHat;
import java.util.ArrayList;

public class BXHAdapter extends ArrayAdapter<BaiHat> {
    Context context;
    ArrayList<BaiHat> bh = new ArrayList<>();
    PopupMenu popupMenu;
    Menu menu;
    public BXHAdapter(@NonNull Context context, ArrayList<BaiHat> bh) {
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

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_bhbxh, parent, false);
        }
        BaiHat currentSong = bh.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_music_namebxh);
        TextView tvSubtitle = listItem.findViewById(R.id.tv_music_subtitlebxh);
        tvTitle.setTextColor(Color.WHITE);
        tvSubtitle.setTextColor(Color.WHITE);
        ImageView imv = listItem.findViewById(R.id.img_menubxh);
        taomenu(imv, currentSong);
        tvTitle.setText(currentSong.getTitle());
        tvSubtitle.setText(currentSong.getSubTitle());
        return listItem;
    }
    public void taomenu(ImageView iv, BaiHat bh)
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

