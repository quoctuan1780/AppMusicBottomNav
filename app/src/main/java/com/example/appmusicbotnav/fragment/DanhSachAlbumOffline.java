package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.AlbumAdapter;
import com.example.appmusicbotnav.model.Album;
import com.example.appmusicbotnav.model.AlbumArraylist;
import com.example.appmusicbotnav.model.BaiHat;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DanhSachAlbumOffline extends Fragment {
    private View view;
    private final int MY_PERMISSION_REQUEST = 1;
    private ArrayList<Album> albums;
    private AlbumAdapter adapter;
    private ListView lv_album;
    private Toolbar toolbar;
    private PopupMenu dropMenu;
    private Menu menu;
    private ImageButton sort;
    private SearchView sv_album;
    private static ArrayList<Album> albums1;
    private RelativeLayout rl_album_offline;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        lv_album = (ListView) view.findViewById(R.id.lv_album_off);
        toolbar = (Toolbar) view.findViewById(R.id.tb_album);
        sort = (ImageButton) view.findViewById(R.id.ib_sapxep_album_off);
        sv_album = (SearchView) view.findViewById(R.id.sv_timkiem_album);
        rl_album_offline = (RelativeLayout) view.findViewById(R.id.rl_album_offline);
        albums  = new ArrayList<>();
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(R.color.gray_color);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        chonalbum();
        khoitaomenusapxepalbum();
        rl_album_offline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        laynhac();
        khoitaotimkiemalbum();
        albums1 = albums;
        super.onResume();
    }

    @Override
    public void onStop() {
        if(adapter != null)
            adapter.clear();
        super.onStop();
    }

    private void chonalbum(){
        lv_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Album albumhientai;
                albumhientai = adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("album", albumhientai.getListBh());
                NavHostFragment.findNavController(DanhSachAlbumOffline.this)
                        .navigate(R.id.action_frag_album_off_to_frag_dsbhoff, bundle);
            }
        });
    }

    private void khoitaotimkiemalbum(){
        final ArrayList<Album> listClone = new ArrayList<>();
        sv_album.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new AlbumAdapter(getContext(), albums);
                    lv_album.setAdapter(adapter);
                    return false;
                }
                else{
                    listClone.clear();
                    for(Album ab : albums)
                        if(ab.getTenAlbum().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(ab);
                        }
                    adapter = new AlbumAdapter(getContext(), listClone);
                    lv_album.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    private void khoitaomenusapxepalbum(){
        dropMenu = new PopupMenu(getContext(), sort);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_sort, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_tangdan:
                        sapxeptangdan();
                        adapter.notifyDataSetChanged();
                        return true;
                    case R.id.item_giamdan:
                        sapxepgiamdan();
                        adapter.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropMenu.show();
            }
        });
    }

    private void sapxeptangdan(){
        Collections.sort(albums, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return o1.getTenAlbum().compareTo(o2.getTenAlbum());
            }
        });
    }

    private void sapxepgiamdan(){
        Collections.sort(albums, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                return o2.getTenAlbum().compareTo(o1.getTenAlbum());
            }
        });
    }

    //Phần khởi tạo bài hát từ bộ nhớ máy

    private void laynhac(){
        BaiHat bh;
        ArrayList<BaiHat> baiHats;
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
        i = 0;

        while(i < listNameAlbum.size()){
            Cursor songCursor = contentResolver.query(uri, null, null, null, null);
            baiHats = new ArrayList<>();
            while (songCursor.moveToNext()){
                if((songCursor.getInt(songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)) == listNameAlbum.get(i).getIdAlbum())) {
                    String tenbh = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String tencs = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String duongdan = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    bh = new BaiHat(tenbh, tencs, duongdan);
                    baiHats.add(bh);
                }
            }
            Album album = new Album(listNameAlbum.get(i).getTenAlbum(), baiHats);
            albums.add(album);
            i++;
            songCursor.close();
        }
        adapter = new AlbumAdapter(getContext(), albums);
        lv_album.setAdapter(adapter);
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
