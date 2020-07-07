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
import com.example.appmusicbotnav.modelOnline.Casi;
import java.util.ArrayList;

public class CasiOnlineAdapter extends ArrayAdapter<Casi> {
    private Context context;
    private ArrayList<Casi> casiArrayList;

    public CasiOnlineAdapter(@NonNull Context context,  ArrayList<Casi> casiArrayList) {
        super(context, 0, casiArrayList);
        this.context = context;
        this.casiArrayList = casiArrayList;
    }

    @Override
    public int getCount() {
        return casiArrayList.size();
    }

    @Nullable
    @Override
    public Casi getItem(int position) {
        return casiArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem = convertView;
        if(listitem == null){
            listitem = LayoutInflater.from(context).inflate(R.layout.item_casi_online_all, parent, false);
        }
        Casi currentCasi;
        currentCasi = getItem(position);
        TextView tv_tencasi = listitem.findViewById(R.id.tv_ten_casi_online_all);
        TextView tv_soluongbh = listitem.findViewById(R.id.tv_slbai_casi_online_all);
        tv_tencasi.setText(currentCasi.getTenCaSi());
        tv_soluongbh.setText("Số lượng bài hát: " + currentCasi.getListBaiHat().size()+"");
        return listitem;
    }
}
