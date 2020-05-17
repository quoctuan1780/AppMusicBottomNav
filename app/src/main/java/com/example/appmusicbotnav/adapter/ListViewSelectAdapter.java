package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.model.BaiHat;
import java.util.ArrayList;

public class ListViewSelectAdapter extends ArrayAdapter<BaiHat> {
    Context context;
    ArrayList<BaiHat> baiHatList;

    public ListViewSelectAdapter(Context context, ArrayList<BaiHat> bh){
        super(context, 0, bh);
        this.context = context;
        this.baiHatList = bh;
    }

    @Override
    public int getCount() {
        return baiHatList.size();
    }

    @Override
    public BaiHat getItem(int position) {
        return baiHatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_chonbaihat, parent, false);
        }
        BaiHat currentSong = baiHatList.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_tenbaihat);
        tvTitle.setTextColor(Color.WHITE);

        TextView tvSubtitle = listItem.findViewById(R.id.tv_tencasi);
        tvSubtitle.setTextColor(Color.WHITE);
        ImageView imageView = listItem.findViewById(R.id.iv_chonbaihat);
        tvTitle.setText(currentSong.getTitle());
        tvSubtitle.setText(currentSong.getSubTitle());
        if(currentSong.getChecked())
            imageView.setImageResource(R.drawable.ic_ticked_24dp);
        else
            imageView.setImageResource(R.drawable.ic_add_24dp);
        return listItem;
    }

    public void updateAdapter(ArrayList<BaiHat> bh){
        this.baiHatList = bh;
        notifyDataSetChanged();
    }
}
