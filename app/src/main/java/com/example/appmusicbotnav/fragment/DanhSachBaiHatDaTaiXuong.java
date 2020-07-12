package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DanhSachBaiHatDaTaiXuong extends Fragment {
    private View view;
    private ArrayList<BaiHat> listBaihat = new ArrayList<>();
    private static ArrayList<BaiHat> listTruyen = new ArrayList<>();
    private ListView listView;
    private BaiHatAdapter adapter;
    private ImageButton ib_sort;
    private SearchView searchView;
    private Toolbar toolbar;
    private PopupMenu dropMenu;
    private Menu menu;
    private RelativeLayout relativeLayout;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_baihatdataixuong, container, false);
        searchView = (SearchView) view.findViewById(R.id.sv_timkiem_baihat_taixuong);
        toolbar = (Toolbar) view.findViewById(R.id.tb_baihat_dataixuong);
        listView = (ListView) view.findViewById(R.id.lv_bai_hat_tai_xuong);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_baihat_dataixuong);
        ib_sort = (ImageButton) view.findViewById(R.id.ib_sort_baihat_taixuong);
        setHasOptionsMenu(true);
        toolbar.setBackgroundColor(R.color.gray_color);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
        layBaiHatDaTaiXuong();
        khoitaomenuSapxep();
        khoitaotimkiem(listBaihat);
        phatnhac();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
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

    private void layBaiHatDaTaiXuong() {
        BaiHat bh;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%/Music/%"}, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            do {
                String tenbh = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String tencs = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duongdan = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                bh = new BaiHat(tenbh, tencs, duongdan);
                listBaihat.add(bh);
            } while (songCursor.moveToNext());
        }
        songCursor.close();
        listTruyen = listBaihat;
        adapter = new BaiHatAdapter(getContext(), listBaihat);
        listView.setAdapter(adapter);
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
                    adapter.updateAdapter(listClone);
                    listTruyen = listClone;
                }
                return false;
            }
        });
    }

    private void khoitaomenuSapxep(){
        dropMenu = new PopupMenu(getContext(), ib_sort);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_sort, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_tangdan:
                        sapxeptangdan();
                        adapter.updateAdapter(listTruyen);
                        return true;
                    case R.id.item_giamdan:
                        sapxepgiamdan();
                        adapter.updateAdapter(listTruyen);
                        return true;
                }
                return false;
            }
        });

        ib_sort.setOnClickListener(new View.OnClickListener() {
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
}
