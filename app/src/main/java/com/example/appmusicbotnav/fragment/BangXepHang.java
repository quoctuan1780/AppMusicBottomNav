package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BXHAdapter;
import com.example.appmusicbotnav.model.BaiHat;

import java.util.ArrayList;

public class BangXepHang extends Fragment {
    ViewFlipper v_flipper;
    private View view;
    BXHAdapter adapter;
    ListView listView;
    ImageView imgmenu;
    PopupMenu dropMenu;
    Menu menu;
    SearchView searchView;
    ArrayList<BaiHat> dsbh;
    final int MY_PERMISSION_REQUEST =1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bangxephang, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int images[]= {R.drawable.poster1,R.drawable.poster3,R.drawable.poster2,R.drawable.poster4};
        v_flipper = view.findViewById(R.id.imgview_bhx);
        listView = view.findViewById(R.id.lv_bxh);
        for(int i = 0; i < images.length; i++)
        {
            flipperImages(images[i]);
        }
         dsbh = new ArrayList<>();
        khoitaoquyentruycap();
        Button vn = view.findViewById(R.id.bxhVN);
        vn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsbh=laynhac();
                ArrayList<BaiHat> top100;
                top100 = Top100VN(dsbh);
                adapter = new BXHAdapter(getContext(), top100);
                listView.setAdapter(adapter);
            }
        });
        Button usuk = view.findViewById(R.id.bxhUSUK);
        usuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsbh=laynhac();
                ArrayList<BaiHat> top100us;
                top100us = Top100USUK(dsbh);
                adapter = new BXHAdapter(getContext(), top100us);
                listView.setAdapter(adapter);
            }
        });
        Button asia = view.findViewById(R.id.bxhAsia);
        asia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsbh=laynhac();
                ArrayList<BaiHat> top100as ;
                top100as = Top100Asia(dsbh);
                adapter = new BXHAdapter(getContext(), top100as);
                listView.setAdapter(adapter);
            }
        });
        Button kpop = view.findViewById(R.id.bxhKPOP);
        kpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsbh=laynhac();
                ArrayList<BaiHat> top100k;
                top100k = Top100KPOP(dsbh);
                adapter = new BXHAdapter(getContext(), top100k);
                listView.setAdapter(adapter);
            }
        });
        searchView = view.findViewById(R.id.search_bxh);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                phatnhac();
            }
        });


    }
    public ArrayList<BaiHat> Top100VN(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<4; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }
    public ArrayList<BaiHat> Top100USUK(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<5; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }
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
                Log.i("TAG", duongdan);
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
    public ArrayList<BaiHat> Top100Asia(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i < 6; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }
    public ArrayList<BaiHat> Top100KPOP(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<3; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(view.getContext());
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval((30));
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(view.getContext(), android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(view.getContext(), android.R.anim.slide_out_right);

    }
    private void khoitaomenu(){
        dropMenu = new PopupMenu(getContext(), imgmenu);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_bxh_kbeat, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download_kbeat:
                        // chưa viết luôn
                        return true;
                    case R.id.themvaoplaylist_kbeat:
                        // them vao playlist chua  viet
                        return true;
                }
                return false;
            }
        });

        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropMenu.show();
            }
        });
    }
        public void down()
        {
        //bla bla
        }
    private void phatnhac(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putExtra("list", dsbh);
                startActivity(phatnhac);
            }
        });
    }
    private void timkiem(){
        final ArrayList<BaiHat> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BXHAdapter(getContext(), dsbh);
                    listView.setAdapter(adapter);

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
        adapter = new BXHAdapter(getContext(), listBh);

        listView.setAdapter(adapter);
    }
        private void hienthiBH(ArrayList<BaiHat> listBh){
        adapter = new BXHAdapter(getContext(), listBh);

        listView.setAdapter(adapter);
    }

}
