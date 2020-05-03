package com.example.appmusicbotnav.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;


public class DanhSachBaiHatOffline extends Fragment {
    View view;
    Toolbar toolbar;
    ArrayList<BaiHat> listBaihat;
    ListView listView;
    public DanhSachBaiHatOffline(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        khoiaobaihat();
        view =  inflater.inflate(R.layout.fragment_frg_baihat, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_baihat);
        toolbar.setTitle("Chọn bài hát");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

//    private void khoiaobaihat(){
//        BaiHat bh1 = new BaiHat("Anh thấy mình nhớ em", "Tống Gia Vĩ", "C:\\Users");
//        BaiHat bh2 = new BaiHat("Lại nhớ người yêu", "Quang Lập", "C:\\Users");
//        listBaihat = new ArrayList<>();
//        listBaihat.add(bh1);
//        listBaihat.add(bh2);
//        BaiHatAdapter adapter = new BaiHatAdapter(getContext(), listBaihat);
//        listView = view.findViewById(R.id.lv_danhsachbaihat);
//        listView.setAdapter(adapter);
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ((AppCompatActivity)getActivity()).onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
