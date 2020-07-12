package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.AlbumArraylist;
import java.util.ArrayList;

public class ThuVien extends Fragment {
    private View view;
    private Toolbar toolbar;
    private int soluongbh = 0, soluongpl = 0, soLuongBhTaiXuong= 0, soLuongAlbum = 0;
    private final int MY_PERMISSION_REQUEST = 1;
    private PlaylistDBOffline playlistDBOffline;
    public ThuVien() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        playlistDBOffline = new PlaylistDBOffline(getContext(), "music.sqlite", null, 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thuvien, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_thuvien);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tv_baihat = (TextView) view.findViewById(R.id.tv_sl_baihat);
        TextView tv_playlist = (TextView) view.findViewById(R.id.tv_sl_playlist);
        TextView tv_bhtaixuong = (TextView) view.findViewById(R.id.tv_sl_bh_taixuong);
        TextView tv_album = (TextView) view.findViewById(R.id.tv_sl_album);
        soluongbh = demsoluongbaihat();
        tv_baihat.setText(soluongbh+"");
        soLuongBhTaiXuong = demSoBaiHatTaiXuong();
        tv_bhtaixuong.setText(soLuongBhTaiXuong+"");
        soLuongAlbum = demSoLuongAlbum();
        tv_album.setText(soLuongAlbum+"");
        try {
            Cursor cursor = playlistDBOffline.DemSoLuongPlaylist();
            if(cursor.moveToNext()){
                soluongpl = Integer.parseInt(cursor.getString(0));
                tv_playlist.setText(soluongpl+"");
            }
        }catch(Exception e){
            tv_playlist.setText(0+"");
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ll_baihat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_thuvien_to_dsbh);
            }
        });

        view.findViewById(R.id.ll_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_thuvien_playlist);
            }
        });

        view.findViewById(R.id.ll_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_item_canhan_to_frag_album_off);
            }
        });

        view.findViewById(R.id.ll_baihattaixuong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_item_canhan_to_baiHatDaTaiXuong);
            }
        });
    }

    //Phần khởi tạo bài hát từ bộ nhớ máy
    private int demsoluongbaihat(){
        int sl = 0;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, null, null, null);
        if(songCursor != null && songCursor.moveToFirst()){
            do{
                sl++;
            }  while(songCursor.moveToNext());
        }
        songCursor.close();
        return sl;
    }

    private int demSoBaiHatTaiXuong(){
        int sl = 0;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%/Music/%"}, null);
        if(songCursor != null && songCursor.moveToFirst()){
            do{
                sl++;
            }  while(songCursor.moveToNext());
        }
        songCursor.close();
        return sl;
    }

    private int demSoLuongAlbum(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor albumCursor = contentResolver.query(uri, null, null, null, null);
        ArrayList<AlbumArraylist> listNameAlbum = new ArrayList<>();
        if(albumCursor != null && albumCursor.moveToFirst()){
            do{
                String tenAlbum = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                int AlbumId = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                AlbumArraylist albumArraylist = new AlbumArraylist(AlbumId, tenAlbum);
                listNameAlbum.add(albumArraylist);
            }  while(albumCursor.moveToNext());
        }
        albumCursor.close();
        int i = 0;
        if(listNameAlbum.size() > 1) {
            while (i < listNameAlbum.size()) {
                int j = i + 1;
                while (listNameAlbum.size() > j)
                    if (listNameAlbum.get(i).getTenAlbum().equals(listNameAlbum.get(j).getTenAlbum())) {
                        listNameAlbum.remove(j);
                    } else j++;
                i++;
            }
        }
        return listNameAlbum.size();
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
