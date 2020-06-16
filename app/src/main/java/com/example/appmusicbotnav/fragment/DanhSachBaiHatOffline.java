package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DanhSachBaiHatOffline extends Fragment {
    private View view;
    private Toolbar toolbar;
    private ArrayList<BaiHat> listBaihat = new ArrayList<>();
    public static ArrayList<BaiHat> listTruyen = new ArrayList<>();
    private ListView listView;
    private SearchView searchView;
    private BaiHatAdapter adapter;
    private ImageButton sort;
    private PopupMenu dropMenu;
    private Menu menu;
    private static final int MY_PERMISSION_REQUEST = 1;

    public DanhSachBaiHatOffline(){

    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_danhsachbaihat, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_baihat);
        toolbar.setBackgroundColor(R.color.gray_color);
        searchView = (SearchView) view.findViewById(R.id.sv_timkiembaihatoff);
        listView = view.findViewById(R.id.lv_danhsachbaihat);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sort = (ImageButton) view.findViewById(R.id.ib_sort);

        if(getArguments() != null){
            try {
                ArrayList<BaiHat> listbh = (ArrayList<BaiHat>) getArguments().getSerializable("listbh");
                listTruyen = listbh;
                khoitaobaihat(listbh);
            }catch(Exception e){
                try{
                    ArrayList<BaiHat> listbh = (ArrayList<BaiHat>) getArguments().getSerializable("album");
                    listTruyen = listbh;
                    khoitaobaihat(listbh);
                }catch (Exception eb){

                }
            }
        }
        else {
            laynhac();
            listTruyen = listBaihat;
        }
        khoitaotimkiem(listTruyen);
        khoitaomenuSapxep();
        phatnhac();
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_baihat);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(getArguments() != null && getArguments().getInt("key") == 1)
                NavHostFragment.findNavController(DanhSachBaiHatOffline.this)
                .navigate(R.id.action_frag_dsbhoff_to_frag_playlist);
            else
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void phatnhac(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putExtra("list", listTruyen);
                startActivity(phatnhac);
            }
        });
    }

    private void khoitaotimkiem(final ArrayList<BaiHat> listbh){
        final ArrayList<BaiHat> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BaiHatAdapter(getContext(), listbh);
                    listView.setAdapter(adapter);
                    listTruyen = listbh;
                    return false;
                }
                else{
                    listClone.clear();
                    for(BaiHat bh : listbh)
                        if(bh.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(bh);
                        }
                    khoitaobaihat(listClone);
                    listTruyen = listClone;
                 }
                return false;
            }
        });
    }

    private void khoitaomenuSapxep(){
        dropMenu = new PopupMenu(getContext(), sort);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_sort, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_tangdan:
                        sapxeptangdan();
                        khoitaobaihat(listTruyen);
                        return true;
                    case R.id.item_giamdan:
                        sapxepgiamdan();
                        khoitaobaihat(listTruyen);
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
        Collections.sort(listTruyen, new Comparator<BaiHat>() {
            @Override
            public int compare(BaiHat o1, BaiHat o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }

    private void sapxepgiamdan(){
        Collections.sort(listTruyen, new Comparator<BaiHat>() {
            @Override
            public int compare(BaiHat o1, BaiHat o2) {
                return o2.getTitle().compareTo(o1.getTitle());
            }
        });
    }

    private void khoitaobaihat(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);
        listView.setAdapter(adapter);
    }


    //Phần khởi tạo bài hát từ bộ nhớ máy

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
                bh = new BaiHat(tenbh, tencs, duongdan);
                listBaihat.add(bh);
            }  while(songCursor.moveToNext());
        }
        songCursor.close();
        adapter = new BaiHatAdapter(getContext(), listBaihat);
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
