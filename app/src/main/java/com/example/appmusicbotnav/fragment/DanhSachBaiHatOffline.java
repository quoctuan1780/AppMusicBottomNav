package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class DanhSachBaiHatOffline extends Fragment {
    View view;
    Toolbar toolbar;
    ArrayList<BaiHat> listBaihat = new ArrayList<>();
    ArrayList<BaiHat> listTruyen = new ArrayList<>();
    ListView listView;
    SearchView searchView;
    BaiHatAdapter adapter;
    HashMap<String, BaiHat> hmSearch;
    ImageButton sort;
    PopupMenu dropMenu;
    Menu menu;
    Bundle b = new Bundle();
    public DanhSachBaiHatOffline(){

    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_baihat, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_baihat);
        toolbar.setTitle("Chọn bài hát");
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
        khoitaobaihat();
        listTruyen = listBaihat;
        khoitaotimkiem();
        khoitaomenuSapxep();
        phatnhac();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ((AppCompatActivity)getActivity()).onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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

    private void khoitaotimkiem(){
        final ArrayList<BaiHat> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BaiHatAdapter(getContext(), listBaihat);
                    listView.setAdapter(adapter);
                    listTruyen = listBaihat;
                    return false;
                }
                else{
                    listClone.clear();
                    for(BaiHat bh : listBaihat)
                        if(bh.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(bh);
                            khoitaobaihat(listClone);
                            listTruyen = listClone;
                        }
                    if(listClone.isEmpty()) khoitaobaihat(listClone);
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

    private void khoitaobaihat(){
        BaiHat bh1 = new BaiHat("Điều anh biết", "Chi Dân", R.raw.dieu_anh_biet);
        BaiHat bh2 = new BaiHat("Đời hư ảo đưa em vào cơn mê", "Mix nhạc", R.raw.doi_hu_ao_dua_em_vao_con_me);
        BaiHat bh3 = new BaiHat("Bad Liar", "Nightcore", R.raw.bad_liar);
        BaiHat bh4 = new BaiHat("Từng trao nhau", "Con vịt", R.raw.tung_trao_nhau);
        BaiHat bh5 = new BaiHat("Anh thấy mình nhớ em", "Tống Gia Vĩ", R.raw.anh_thay_minh_nho_em);
        BaiHat bh6 = new BaiHat("Lại nhớ người yêu", "Quang Lập", R.raw.lai_nho_nguoi_yeu);
        BaiHat bh7 = new BaiHat("Tay trái chỉ trăng", "Tát Đỉnh Đỉnh", R.raw.tay_trai_chi_trang);
        listBaihat.add(bh1);
        listBaihat.add(bh3);
        listBaihat.add(bh2);
        listBaihat.add(bh4);
        listBaihat.add(bh5);
        listBaihat.add(bh6);
        listBaihat.add(bh7);
        adapter = new BaiHatAdapter(getContext(), listBaihat);
        listView.setAdapter(adapter);
    }

    private void khoitaobaihat(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);
        listView.setAdapter(adapter);
    }
}
