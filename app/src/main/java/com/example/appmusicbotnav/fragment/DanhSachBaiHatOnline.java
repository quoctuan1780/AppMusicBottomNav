package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatOnlineAdapter;
import com.example.appmusicbotnav.modelOnline.Baihat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DanhSachBaiHatOnline extends Fragment {
    private View view;
    private Toolbar toolbar;
    private BaiHatOnlineAdapter adapter;
    private SearchView sv_baihat_online;
    private ArrayList<Baihat> baihatarraylist = new ArrayList<>();
    private static ArrayList<Baihat> listtruyen = new ArrayList<>();
    private ListView lv_dsbh;
    private ImageButton ib_sort_online;
    private Menu menu;
    private RelativeLayout rl_baihat_online;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danhsachbaihatonline, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.tb_baihat_online);
        sv_baihat_online = (SearchView) view.findViewById(R.id.sv_timkiembaihat_online);
        lv_dsbh = (ListView) view.findViewById(R.id.lv_danhsachbaihat_online);
        ib_sort_online = (ImageButton) view.findViewById(R.id.ib_sort_online);
        rl_baihat_online = (RelativeLayout) view.findViewById(R.id.rl_baihat_online);
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
        try{
            laydanhsachbaihat();
            khoitaomenuSapxep();
            khoitaotimkiem(baihatarraylist);
            phatnhac();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
        }
        rl_baihat_online.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void phatnhac(){
        lv_dsbh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) listtruyen);
                try {
                    startActivity(phatnhac);
                }catch (Exception e){

                }
            }
        });
    }

    private void laydanhsachbaihat() {
        try{
            baihatarraylist = getArguments().getParcelableArrayList("listbh");
            listtruyen = baihatarraylist;
            khoitaolistbaihat(baihatarraylist);
        }catch(Exception e){
            Toast.makeText(getActivity(), "Không thể lấy danh sách bài hát", Toast.LENGTH_LONG).show();
        }
    }

    private void khoitaolistbaihat(ArrayList<Baihat> baihatarraylist) {
        adapter = new BaiHatOnlineAdapter(getContext(), baihatarraylist);
        lv_dsbh.setAdapter(adapter);
    }

    private void khoitaotimkiem(final ArrayList<Baihat> listbh){
        final ArrayList<Baihat> listClone = new ArrayList<>();
        sv_baihat_online.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BaiHatOnlineAdapter(getContext(), listbh);
                    lv_dsbh.setAdapter(adapter);
                    listtruyen = listbh;
                    return false;
                }
                else{
                    listClone.clear();
                    for(Baihat bh : listbh)
                        if(bh.getTenBaiHat().toLowerCase().contains(newText.toLowerCase()) ||
                                bh.getTenTacGia().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(bh);
                        }
                    khoitaolistbaihat(listClone);
                    listtruyen = listClone;
                }
                return false;
            }
        });
    }

    private void khoitaomenuSapxep(){
        final PopupMenu dropMenu = new PopupMenu(getContext(), ib_sort_online);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_sort, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_tangdan:
                        sapxeptangdan();
                        khoitaolistbaihat(listtruyen);
                        return true;
                    case R.id.item_giamdan:
                        sapxepgiamdan();
                        khoitaolistbaihat(listtruyen);
                        return true;
                }
                return false;
            }
        });

        ib_sort_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropMenu.show();
            }
        });
    }

    private void sapxeptangdan(){
        Collections.sort(listtruyen, new Comparator<Baihat>() {
            @Override
            public int compare(Baihat o1, Baihat o2) {
                return o1.getTenBaiHat().compareTo(o2.getTenBaiHat());
            }
        });
    }

    private void sapxepgiamdan(){
        Collections.sort(listtruyen, new Comparator<Baihat>() {
            @Override
            public int compare(Baihat o1, Baihat o2) {
                return o2.getTenBaiHat().compareTo(o1.getTenBaiHat());
            }
        });
    }
}
