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
import com.example.appmusicbotnav.model.Album;
import java.util.ArrayList;

public class AlbumAdapter extends ArrayAdapter<Album> {
    private Context context;
    ArrayList<Album> albumArrayList;

    public AlbumAdapter (Context context, ArrayList<Album> albumArrayList) {
        super(context, 0, albumArrayList);
        this.context = context;
        this.albumArrayList = albumArrayList;
    }

    @Override
    public int getCount() {
        return albumArrayList.size();
    }

    @Nullable
    @Override
    public Album getItem(int position) {
        return albumArrayList.get(position);
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
            listItem = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        }
        Album currentAlbum;
        currentAlbum = getItem(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_tenplaylist);
        tvTitle.setText(currentAlbum.getTenAlbum());
        return listItem;
    }
}
