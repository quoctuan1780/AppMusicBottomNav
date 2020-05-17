package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.AlbumAdapter;
import com.example.appmusicbotnav.model.Album;
import com.example.appmusicbotnav.model.BaiHat;
import java.util.ArrayList;

public class DanhSachAlbumOffline extends Fragment {
    private View view;
    private final int MY_PERMISSION_REQUEST = 1;
    ArrayList<Album> albums = new ArrayList<>();
    AlbumAdapter adapter;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        listView = (ListView) view.findViewById(R.id.lv_album_off);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        khoitaoquyentruycap();
        laynhac();
        super.onViewCreated(view, savedInstanceState);
    }

    public void layAlbum(){

    }

    //Phần khởi tạo bài hát từ bộ nhớ máy
    private void khoitaoquyentruycap(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }else{
            Log.i("TAG", "Da khoi tao quyen truy cap: ");
        }
    }

    private void laynhac(){
        BaiHat bh;
        ArrayList<BaiHat> baiHats;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor albumCursor = contentResolver.query(uri, null, null, null, null);
        Cursor songCursor = albumCursor;
        if(albumCursor != null && songCursor.moveToFirst()){
            do{
                baiHats = new ArrayList<>();
                String tenAlbum = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                int AlbumId = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                while (songCursor.moveToNext() && (songCursor.getInt(songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)) == AlbumId)){
                    String tenbh = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String tencs = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String duongdan = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    bh = new BaiHat(tenbh, tencs, duongdan);
                    baiHats.add(bh);
                }
                Album album = new Album(tenAlbum, baiHats);
                albums.add(album);
            }  while(albumCursor.moveToNext());
        }
        songCursor.close();
        adapter = new AlbumAdapter(getContext(), albums);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permision Granted", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Permision not grant", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
        }
    }
}
