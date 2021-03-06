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
import android.widget.SearchView;
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

public class ThemBaiHatVaoPlaylistOff extends Fragment {
    private View view;
    private Toolbar toolbar;
    private ListViewSelectAdapter adapter;
    private ListView listView;
    private ArrayList<BaiHat> listBaihat = new ArrayList<>();
    public ArrayList<BaiHat> listBaihatClone = null;
    private ArrayList<BaiHat> baiHatArrayList = new ArrayList<>();
    private Bundle bundle;
    private PlaylistDBOffline playlistDBOffline;
    private BaiHatDBOffline baiHatDBOffline;
    private static final int MY_PERMISSION_REQUEST = 1;
    private SearchView searchView;
    public ThemBaiHatVaoPlaylistOff(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = this.getArguments();
        khoitaoquyentruycap();
        playlistDBOffline = new PlaylistDBOffline(getContext(), "music.sqlite", null, 1);
        baiHatDBOffline = new BaiHatDBOffline(getContext(), "music.sqlite", null, 1);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thembaihatvao_playlist, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_chonbaihat_listview_off);
        toolbar.setBackgroundColor(R.color.gray_color);
        searchView = (SearchView) view.findViewById(R.id.sv_timkhiem_bh_playlist_off);
        listView = view.findViewById(R.id.lv_chonbaihat_playlist_off);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playlistDBOffline.Taobang();
        baiHatDBOffline.Taobang();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_playlist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        laynhac();
        listBaihatClone = listBaihat;
        adapter = new ListViewSelectAdapter(getContext(), listBaihatClone);
        listView.setAdapter(adapter);
        chonbaihat();
        khoitaotimkiem();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavHostFragment.findNavController(ThemBaiHatVaoPlaylistOff.this)
                        .navigate(R.id.action_item_chonBH_playlist_off_to_frag_playlist);
                return true;
            case R.id.item_tao_playlist:
                String tenPl = bundle.getString("tenplaylist");
                playlistDBOffline.ThemPlaylist(tenPl);
                Cursor cursor = playlistDBOffline.LayPlayList(tenPl);
                if(cursor != null){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    for (BaiHat bh : listBaihatClone){
                        if(bh.getChecked()){
                            String tenbh = bh.getTitle();
                            String tencs = bh.getSubTitle();
                            String duongdan = bh.getPath();
                            baiHatDBOffline.ThemBaiHatPl(tenbh, tencs, duongdan, id);
                        }
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putInt("taoplthanhcong", 1);
                NavHostFragment.findNavController(ThemBaiHatVaoPlaylistOff.this)
                        .navigate(R.id.action_item_chonBH_playlist_off_to_frag_playlist, bundle);
                break;
            }
        return super.onOptionsItemSelected(item);
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
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, null, null, null);
        if(songCursor != null && songCursor.moveToFirst()){
            do{
                String tenbh = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String tencs = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duongdan = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.i("TAG", duongdan);
                bh = new BaiHat(tenbh, tencs, duongdan);
                listBaihat.add(bh);
            }  while(songCursor.moveToNext());
        }
        songCursor.close();
        adapter = new ListViewSelectAdapter(getContext(), listBaihat);
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

    private void chonbaihat(){
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(baiHatArrayList.size() == 0) {
                    BaiHat bh = listBaihatClone.get(position);
                    if (bh.getChecked() == false)
                        bh.setCheckBox(true);
                    else bh.setCheckBox(false);
                    listBaihatClone.set(position, bh);
                    adapter.updateAdapter(listBaihatClone);
                }else{
                    BaiHat bh = baiHatArrayList.get(position);
                    if (bh.getChecked() == false)
                        bh.setCheckBox(true);
                    else bh.setCheckBox(false);
                    baiHatArrayList.set(position, bh);
                    adapter.updateAdapter(baiHatArrayList);
                }
            }
        });
    }

    private void khoitaotimkiem(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    baiHatArrayList.clear();
                    adapter.updateAdapter(listBaihatClone);
                    return true;
                }
                else{
                    baiHatArrayList.clear();
                    for(BaiHat bh : listBaihatClone) {
                        if (bh.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                            baiHatArrayList.add(bh);
                        }
                    }
                    adapter.updateAdapter(baiHatArrayList);
                    return true;
                }
            }
        });
    }

}
