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
import com.example.appmusicbotnav.modelOnline.Album;
import java.util.ArrayList;

public class AlbumOnlineAdapter extends ArrayAdapter<Album> {
    private ArrayList<Album> albums;
    private Context context;
    public AlbumOnlineAdapter(@NonNull Context context, ArrayList<Album> albums){
        super(context, 0, albums);
        this.context = context;
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Nullable
    @Override
    public Album getItem(int position) {
        return albums.get(position);
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
            listItem = LayoutInflater.from(context).inflate(R.layout.item_album_online_all, parent, false);
        }
        Album currentAlbum;
        currentAlbum = getItem(position);
        TextView tv_tenalbum = listItem.findViewById(R.id.tv_ten_album_online_all);
        TextView tv_slbai = listItem.findViewById(R.id.tv_slbai_album_online_all);
        tv_tenalbum.setText(currentAlbum.getTenAlbum());
        tv_slbai.setText(currentAlbum.getSoLuongBai() + "");
        return listItem;
    }
}
