package com.example.appmusicbotnav.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.InputType;
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
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.db.Database;
import com.example.appmusicbotnav.model.PlayList;
import java.util.ArrayList;

public class PlayListSelectAdapter extends ArrayAdapter<PlayList> {
    Context context;
    ArrayList<PlayList> listPlaylist;
    private Menu menuPlaylist;
    private PopupMenu PopMenuPlaylist;
    private Database database;
    private AlertDialog.Builder hopthoainhaptenplaylist;
    private EditText nhap;
    public PlayListSelectAdapter(Context context, ArrayList<PlayList> pl){
        super(context, 0, pl);
        this.context = context;
        this.listPlaylist = pl;
        database = new Database(getContext(), "music.sqlite", null, 1);
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
                        Cursor layplaylist = database.Laydulieu("SELECT * FROM Playlist WHERE Tenplaylist = '"+ tenplaylist + "'");
                        if(layplaylist != null){
                            layplaylist.moveToNext();
                            int id = layplaylist.getInt(0);
                            database.Thucthitruyvan("DELETE FROM Baihatplaylist WHERE IdPlaylist = " + id);
                            database.Thucthitruyvan("DELETE FROM Playlist WHERE Id = " + id);
                            Toast.makeText(getContext(), "Đã xóa playlist", Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();
                            return true;
                        }
                        break;
                    case R.id.item_sua_playlist:
                        suatenplaylist(currentPlaylist);
                        return true;
                    case R.id.item_sua_baihattrong_playlist:
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
                    Cursor layplaylist = database.Laydulieu("SELECT * FROM Playlist WHERE Tenplaylist = '"+ tenplaylist + "'");
                    if(layplaylist != null){
                        layplaylist.moveToNext();
                        int id = layplaylist.getInt(0);
                        database.Thucthitruyvan("UPDATE Playlist SET Tenplaylist = '"+ nhap.getText().toString() + "' " +
                                "WHERE id = " + id);
                        notifyDataSetChanged();
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

    public void updateAdapter(ArrayList<PlayList> playLists){
        this.listPlaylist = playLists;
        notifyDataSetChanged();
    }
}
