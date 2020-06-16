package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.ListViewSelectAdapter;
import com.example.appmusicbotnav.db.BaiHatDBOffline;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;

public class SuaBaiHatTrongPlaylistOff extends Fragment {
    private Toolbar toolbar;
    private ArrayList<BaiHat> listPl, listBh;
    private String tenplaylist;
    private ListView lv_suabaihat;
    private ListViewSelectAdapter adapter;
    private Cursor playlist, baihatplaylist;
    private BaiHatDBOffline baiHatDBOffline;
    private PlaylistDBOffline playlistDBOffline;
    private final int MY_PERMISSION_REQUEST = 1;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_suabaihatplaylist, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_suabaihat_playlist);
        toolbar.setBackgroundColor(R.color.gray_color);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listPl = new ArrayList<>();
        lv_suabaihat = (ListView) view.findViewById(R.id.lv_chonbaihat_suaplaylist_off);
        tenplaylist = getArguments().getString("tenplaylist");
        playlistDBOffline = new PlaylistDBOffline(getContext(), "music.sqlite", null, 1);
        baiHatDBOffline = new BaiHatDBOffline(getContext(), "music.sqlite", null, 1);
        playlist = playlistDBOffline.LayPlayList(tenplaylist);
        listBh = laynhac();
        if(playlist.moveToNext()){
            baihatplaylist = baiHatDBOffline.LayBaiHatPlaylist(playlist.getInt(0));
            while(baihatplaylist.moveToNext()){
                String tenbh = baihatplaylist.getString(1);
                String tencs = baihatplaylist.getString(2);
                String dn = baihatplaylist.getString(3);
                BaiHat bh = new BaiHat(tenbh, tencs, dn, true);
                for(int i = 0; i < listBh.size(); i++)
                    if(listBh.get(i).getPath().equals(dn))
                        listBh.remove(listBh.get(i));
                listPl.add(bh);
            }
        }
        for(BaiHat bh : listBh){
            listPl.add(bh);
        }
        adapter = new ListViewSelectAdapter(getContext(), listPl);
        lv_suabaihat.setAdapter(adapter);
        chonbaihat();
        super.onViewCreated(view, savedInstanceState);
    }

    private void chonbaihat(){
        lv_suabaihat.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv_suabaihat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaiHat bh = listPl.get(position);
                if(bh.getChecked() == false)
                    bh.setCheckBox(true);
                else bh.setCheckBox(false);
                listPl.set(position, bh);
                adapter.updateAdapter(listPl);
            }
        });
    }

    private ArrayList<BaiHat> laynhac(){
        BaiHat bh;
        ArrayList<BaiHat> listlocal = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, null, null, null);
        if(songCursor != null && songCursor.moveToFirst()){
            do{
                String tenbh = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String tencs = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duongdan = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                bh = new BaiHat(tenbh, tencs, duongdan, false);
                listlocal.add(bh);
            }  while(songCursor.moveToNext());
        }
        songCursor.close();
        return listlocal;
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_suabhplaylist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavHostFragment.findNavController(SuaBaiHatTrongPlaylistOff.this)
                    .navigate(R.id.action_frag_suabaihat_playlistoff_to_frag_playlist);
        }
        else{
            switch (item.getItemId()){
                case R.id.item_luubh_suaplaylist:
                {
                    baiHatDBOffline.XoaBaiHatPlaylist(playlist.getInt(0));
                    for(BaiHat bh : listPl){
                        if(bh.getChecked())
                        baiHatDBOffline.ThemBaiHatPl(bh.getTitle(), bh.getSubTitle(), bh.getPath(), playlist.getInt(0));
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("ok", "ok");
                    NavHostFragment.findNavController(SuaBaiHatTrongPlaylistOff.this)
                            .navigate(R.id.action_frag_suabaihat_playlistoff_to_frag_playlist, bundle);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
