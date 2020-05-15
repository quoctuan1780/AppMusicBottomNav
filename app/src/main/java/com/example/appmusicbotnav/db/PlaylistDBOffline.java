package com.example.appmusicbotnav.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class PlaylistDBOffline extends Database {

    public PlaylistDBOffline(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void Taobang(){
        String sql = "CREATE TABLE IF NOT EXISTS PLAYLIST(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TENPLAYLIST VARCHAR(60))";
        Thucthitruyvan(sql);
    }

    public void ThemPlaylist(String tenplaylist){
        String sql = "INSERT INTO PLAYLIST VALUES(null, '"+tenplaylist+"')";
        Thucthitruyvan(sql);
    }

    public void XoaPlaylist(int id){
        String sql = "DELETE FROM BAIHAT WHERE IDPLAYLIST = " + id;
        String sql1 = "DELETE FROM PLAYLIST WHERE ID = " + id;
        Thucthitruyvan(sql);
        Thucthitruyvan(sql1);
    }

    public void SuaTenPlaylist(int id, String tenplaylist){
        String sql = "UPDATE PLAYLIST SET TENPLAYLIST = '"+tenplaylist+"' WHERE ID = " + id;
        Thucthitruyvan(sql);
    }

    public Cursor LayPlaylist(){
        String sql = "SELECT * FROM PLAYLIST";
        return Laydulieu(sql);
    }

    public Cursor LayPlayList(String tenplaylist){
        String sql = "SELECT * FROM Playlist WHERE Tenplaylist = '"+ tenplaylist + "'";
        return Laydulieu(sql);
    }

    public Cursor DemSoLuongPlaylist(){
        String sql = "SELECT COUNT(*) FROM PLAYLIST";
        return Laydulieu(sql);
    }
}
