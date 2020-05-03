package com.example.appmusicbotnav.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;
import java.util.HashMap;


public class DanhSachBaiHatOffline extends Fragment {
    View view;
    Toolbar toolbar;
    ArrayList<BaiHat> listBaihat;
    ListView listView;
    SearchView searchView;
    BaiHatAdapter adapter;
    HashMap<String, BaiHat> hmSearch;
    public DanhSachBaiHatOffline(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_baihat, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_baihat);
        toolbar.setTitle("Chọn bài hát");
        searchView = (SearchView) view.findViewById(R.id.sv_timkiembaihatoff);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        khoitaobaihat();
        final ArrayList<BaiHat> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    khoitaobaihat();
                    return false;
                }
                else{
                    listClone.clear();
                for(BaiHat bh : listBaihat)
                    if(bh.getTitle().toLowerCase().contains(newText.toLowerCase())){
                        listClone.add(bh);
                        khoitaobaihat(listClone);
                    }
                }
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    private void khoitaobaihat(){
        BaiHat bh1 = new BaiHat("Anh thấy mình nhớ em", "Tống Gia Vĩ", "C:\\Users");
        BaiHat bh2 = new BaiHat("Lại nhớ người yêu", "Quang Lập", "C:\\Users");
        BaiHat bh3 = new BaiHat("L", "Quang Lập", "C:\\Users");
        listBaihat = new ArrayList<>();
        listBaihat.add(bh1);
        listBaihat.add(bh2);
        listBaihat.add(bh3);
        adapter = new BaiHatAdapter(getContext(), listBaihat);
        listView = view.findViewById(R.id.lv_danhsachbaihat);
        listView.setAdapter(adapter);
    }

    private void khoitaobaihat(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);
        listView = view.findViewById(R.id.lv_danhsachbaihat);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ((AppCompatActivity)getActivity()).onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
