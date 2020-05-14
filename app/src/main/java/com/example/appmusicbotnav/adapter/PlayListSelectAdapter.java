package com.example.appmusicbotnav.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.SuaBaiHatPlaylist;
import com.example.appmusicbotnav.db.BaiHatDBOffline;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.BaiHat;
import com.example.appmusicbotnav.model.PlayList;

import java.io.Serializable;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PlayListSelectAdapter extends ArrayAdapter<PlayList> {
    Context context;
    ArrayList<PlayList> listPlaylist;
    private Menu menuPlaylist;
    private PopupMenu PopMenuPlaylist;
    private AlertDialog.Builder hopthoainhaptenplaylist;
    private EditText nhap;
    private PlaylistDBOffline playlistDBOffline;
    private BaiHatDBOffline baiHatDBOffline;
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
        PlayList currentPlaylist = listPlaylist.get(position);
        TextView tvTitle = listItem.findViewById(R.id.tv_tenplaylist);
        ImageView iv_menuPlaylist = listItem.findViewById(R.id.iv_menu_playlist);
        tvTitle.setText(currentPlaylist.getTenPlaylist());
        khoitaomenuplaylist(iv_menuPlaylist, currentPlaylist);
        return listItem;
    }

    private void khoitaomenuplaylist(ImageView iv_menuPlaylist, final PlayList currentPlaylist){
        PopMenuPlaylist = new PopupMenu(getContext(), iv_menuPlaylist);
        menuPlaylist = PopMenuPlaylist.getMenu();
        PopMenuPlaylist.getMenuInflater().inflate(R.menu.menu_item_playlist, menuPlaylist);
        PopMenuPlaylist.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_xoa_playlist:
                        String tenplaylist = currentPlaylist.getTenPlaylist();
                        Cursor layplaylist = playlistDBOffline.LayPlayList(tenplaylist);
                        if(layplaylist != null){
                            layplaylist.moveToNext();
                            int id = layplaylist.getInt(0);
                            playlistDBOffline.XoaPlaylist(id);
                            Toast.makeText(getContext(), "Đã xóa playlist", Toast.LENGTH_LONG).show();
                            updateAdapter();
                            return true;
                        }
                        break;
                    case R.id.item_sua_playlist:
                        suatenplaylist(currentPlaylist);
                        return true;
                    case R.id.item_sua_baihattrong_playlist:
                        suabaihattrongplaylist(currentPlaylist);
                        return true;
                }
                return false;
            }
        });

        iv_menuPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopMenuPlaylist.show();
            }
        });
    }

    private void suatenplaylist(final PlayList currentPlaylist){
        hopthoainhaptenplaylist = new AlertDialog.Builder(getContext());
        hopthoainhaptenplaylist.setTitle("Nhập tên playlist");

        nhap = new EditText(getContext());
        nhap.setInputType(InputType.TYPE_CLASS_TEXT);
        hopthoainhaptenplaylist.setView(nhap);

        hopthoainhaptenplaylist.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(nhap.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Tên playlist không được để trống", Toast.LENGTH_LONG).show();
                }
                else{
                    String tenplaylist = currentPlaylist.getTenPlaylist();
                    Cursor layplaylist = playlistDBOffline.LayPlayList(tenplaylist);
                    if(layplaylist != null){
                        layplaylist.moveToNext();
                        int id = layplaylist.getInt(0);
                        playlistDBOffline.SuaTenPlaylist(id, nhap.getText().toString());
                        updateAdapter();
                    }
                }
            }
        });

        hopthoainhaptenplaylist.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        hopthoainhaptenplaylist.show();
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
                    String tenbh = baihatpl.getString(0);
                    String tencs = baihatpl.getString(1);
                    String dn = baihatpl.getString(2);
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

    private void suabaihattrongplaylist(final PlayList currentPlaylist){
        String tenpl = currentPlaylist.getTenPlaylist();
        ArrayList<BaiHat> listbh = currentPlaylist.getBaiHat();
        Intent suaplaylist = new Intent(getContext(),SuaBaiHatPlaylist.class);
        suaplaylist.putExtra("tenplaylist", tenpl);
        getContext().startActivity(suaplaylist);
    }

    public void updateAdapter(){
        this.listPlaylist = DanhSachPlaylist();
        notifyDataSetChanged();
    }
}
