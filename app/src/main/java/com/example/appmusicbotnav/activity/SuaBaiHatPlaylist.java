package com.example.appmusicbotnav.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.ListViewSelectAdapter;
import com.example.appmusicbotnav.db.BaiHatDBOffline;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;

public class SuaBaiHatPlaylist extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<BaiHat> list;
    private String tenplaylist;
    private ListView lv_suabaihat;
    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suabaihatplaylist);
        lv_suabaihat = (ListView) findViewById(R.id.lv_chonbaihat_suaplaylist_off);
        toolbar = (Toolbar) findViewById(R.id.tb_suabaihat_playlist);
        toolbar.setTitle("Sửa bài hát trong playlist");
        toolbar.setBackgroundColor(R.color.gray_color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tenplaylist = getIntent().getStringExtra("tenplaylist");
        PlaylistDBOffline playlistDBOffline = new PlaylistDBOffline(getBaseContext(), "music.sqlite", null, 1);
        BaiHatDBOffline baiHatDBOffline = new BaiHatDBOffline(getBaseContext(), "music.sqlite", null, 1);
        Cursor cursor = playlistDBOffline.LayPlayList(tenplaylist);
        if(cursor.moveToNext()){
            Cursor cursor1 = baiHatDBOffline.LayBaiHatPlaylistAll();
            while(cursor1.moveToNext()){
                Log.i("TAG", cursor1.getString(0));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
