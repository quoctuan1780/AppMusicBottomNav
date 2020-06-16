package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.BXHAdapter;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;

public class GhiAm extends Fragment {
    private static final int MY_PERMISSION_REQUEST = 1;
    private View view;
    private ListView listview;
    SearchView searchview;
    BaiHatAdapter adapter;
    ArrayList<BaiHat> dsbh;

    public GhiAm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ghi_am, container, false);
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dsbh = new ArrayList<>();
        dsbh = laynhac();
        listview = view.findViewById(R.id.lv_danhsachBHghiam);
        hienthiBH(dsbh);
        searchview = view.findViewById(R.id.sv_timkiemBHghiam);
        timkiem();
//
//        listview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // to do something here
//            }
//        });
    }

//    private void khoitaoquyentruycap(){
//        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
//            }else{
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
//            }
//        }else{
//            Log.i("TAG", "Da khoi tao quyen truy cap: ");
//        }
//    }

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
                bh = new BaiHat(tenbh, tencs, duongdan);
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
    private void timkiem(){
        final ArrayList<BaiHat> listClone = new ArrayList<>();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BaiHatAdapter(getContext(), dsbh);
                    listview.setAdapter(adapter);

                    return false;
                }
                else{
                    listClone.clear();
                    for(BaiHat bh : dsbh)
                        if(bh.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(bh);
                            khoitaobaihat(listClone);

                        }
                    if(listClone.isEmpty()) khoitaobaihat(listClone);
                }
                return false;
            }
        });
    }
    private void khoitaobaihat(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);

        listview.setAdapter(adapter);
    }
    private void hienthiBH(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);
        listview.setAdapter(adapter);
    }

}

