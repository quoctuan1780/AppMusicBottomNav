package com.example.appmusicbotnav.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

public class BaiHatDBOffline extends Database {
    public BaiHatDBOffline(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void Taobang(){
        String sql = "CREATE TABLE IF NOT EXISTS BAIHAT(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TENBAIHAT VARCHAR(60)," +
                "TENCASI VARCHAR(60)," +
                "DUONGDAN VARCHAR(250)," +
                "IDPLAYLIST INTEGER REFERENCES PLAYLIST(ID))";
        Thucthitruyvan(sql);
    }
    public void ThemBaiHatPl(String tenbh, String tencs, String dn, int idPlaylist){
        String sql = "INSERT INTO BAIHAT VALUES(null, '" + tenbh + "', '" + tencs + "','" + dn + "', " + idPlaylist + ")";
        Thucthitruyvan(sql);
        Log.i("TAG", "Đã thêm bài hát");
    }

    public Cursor LayBaiHatPlaylist(int idplaylist){
        String sql = "SELECT * FROM BAIHAT WHERE IDPLAYLIST = " + idplaylist;
        return Laydulieu(sql);
    }

    public Cursor LayBaiHatPlaylistAll(){
        String sql = "SELECT * FROM BAIHAT";
        return Laydulieu(sql);
    }
}
