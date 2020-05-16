package com.example.appmusicbotnav.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.db.BaiHatDBOffline;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.BaiHat;
import com.example.appmusicbotnav.model.PlayList;
import java.util.ArrayList;

public class PlayListSelectAdapter extends ArrayAdapter<PlayList> {
    Context context;
    ArrayList<PlayList> listPlaylist;
    private PlaylistDBOffline playlistDBOffline;
    private BaiHatDBOffline baiHatDBOffline;
    private PlayList currentPlaylist;
    public PlayListSelectAdapter(Context context, ArrayList<PlayList> pl){
        super(context, 0, pl);
        this.context = context;
        this.listPlaylist = pl;
        playlistDBOffline = new PlaylistDBOffline(getContext(), "music.sqlite", null, 1);
        baiHatDBOffline = new BaiHatDBOffline(getContext(), "music.sqlite", null, 1);
    }

    @Override
    public int getCount() {
        return listPlaylist.size();
    }

    @Override
    public PlayList getItem(int position) {
        return listPlaylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        }
        currentPlaylist = getItem(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_tenplaylist);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setText(currentPlaylist.getTenPlaylist());
        return listItem;
    }

    public ArrayList<PlayList> DanhSachPlaylist(){
        ArrayList<PlayList> playLists = new ArrayList<>();
        try{
            Cursor playlist = playlistDBOffline.LayPlaylist();
            while(playlist.moveToNext()){
                ArrayList<BaiHat> baiHats = new ArrayList<>();
                int id = playlist.getInt(0);
                String tenPl = playlist.getString(1);
                Cursor baihatpl = baiHatDBOffline.LayBaiHatPlaylist(id);
                while(baihatpl.moveToNext()){
                    String tenbh = baihatpl.getString(1);
                    String tencs = baihatpl.getString(2);
                    String dn = baihatpl.getString(3);
                    BaiHat bh = new BaiHat(tenbh, tencs, dn);
                    baiHats.add(bh);
                }
                PlayList pl = new PlayList(tenPl, baiHats);
                playLists.add(pl);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return playLists;
    }

    public PlayList DanhSachPlaylist(String tenplaylist){
        ArrayList<PlayList> playLists = new ArrayList<>();
        try{
            Cursor playlist = playlistDBOffline.LayPlayList(tenplaylist);
            if(playlist.moveToNext()){
                ArrayList<BaiHat> baiHats = new ArrayList<>();
                int id = playlist.getInt(0);
                String tenPl = playlist.getString(1);
                Cursor baihatpl = baiHatDBOffline.LayBaiHatPlaylist(id);
                while(baihatpl.moveToNext()){
                    String tenbh = baihatpl.getString(1);
                    String tencs = baihatpl.getString(2);
                    String dn = baihatpl.getString(3);
                    BaiHat bh = new BaiHat(tenbh, tencs, dn);
                    baiHats.add(bh);
                }
                PlayList pl = new PlayList(tenPl, baiHats);
                return pl;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateAdapter(){
        this.listPlaylist = DanhSachPlaylist();
        notifyDataSetChanged();
    }
}
