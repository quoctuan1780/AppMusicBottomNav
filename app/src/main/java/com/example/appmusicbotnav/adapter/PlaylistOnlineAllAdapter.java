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
import com.example.appmusicbotnav.modelOnline.Playlist;
import java.util.ArrayList;

public class PlaylistOnlineAllAdapter extends ArrayAdapter<Playlist> {
    private Context context;
    private ArrayList<Playlist> playlistArrayList;

    public PlaylistOnlineAllAdapter(@NonNull Context context, ArrayList<Playlist> playlistArrayList) {
        super(context, 0, playlistArrayList);
        this.context = context;
        this.playlistArrayList = playlistArrayList;
    }

    @Override
    public long getItemId(int position) {
        return playlistArrayList.get(position).getIdPlayList();
    }

    @Nullable
    @Override
    public Playlist getItem(int position) {
        return playlistArrayList.get(position);
    }

    @Override
    public int getCount() {
        return playlistArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_playlist, null);
        }
        Playlist playlistHienTai = playlistArrayList.get(position);
        TextView tv_tenplaylist = (TextView) view.findViewById(R.id.tv_tenplaylist);
        tv_tenplaylist.setText(playlistHienTai.getTenPlayList());
        return view;
    }

    public void updatePlaylist(ArrayList<Playlist> playlistArrayList){
        this.playlistArrayList = playlistArrayList;
        notifyDataSetChanged();
    }
}
