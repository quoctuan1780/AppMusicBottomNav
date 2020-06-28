package com.example.appmusicbotnav.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.AlbumOnlineRandomAdapter;
import com.example.appmusicbotnav.adapter.CasiOnlineRandomAdapter;
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.Casi;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Online extends Fragment  {
    private View view;
    private TextView tv_xemthem_album, tv_xemthem_casi;
    private static ArrayList<Casi> casiArrayList, casiRamdom;
    private static  ArrayList<Album> albumarraylist;
    private RecyclerView rcv_album, rcv_casi;
    private AlbumOnlineRandomAdapter adapter;
    private CasiOnlineRandomAdapter casiOnlineRandomAdapter;

    public Online(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        tv_xemthem_album = (TextView) view.findViewById(R.id.tv_album_xemthem);
        tv_xemthem_casi = (TextView) view.findViewById(R.id.tv_casi_xemthem);
        rcv_album = (RecyclerView) view.findViewById(R.id.rcv_album_online);
        rcv_casi = (RecyclerView) view.findViewById(R.id.rcv_casi_online);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(albumarraylist != null && casiArrayList != null){
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            adapter = new AlbumOnlineRandomAdapter(getContext(), albumarraylist, getParentFragment());
            manager.setOrientation(RecyclerView.HORIZONTAL);
            rcv_album.setLayoutManager(manager);
            rcv_album.setAdapter(adapter);
            LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
            casiOnlineRandomAdapter = new CasiOnlineRandomAdapter(getContext(), casiRamdom, getParentFragment());
            manager1.setOrientation(RecyclerView.HORIZONTAL);
            rcv_casi.setLayoutManager(manager1);
            rcv_casi.setAdapter(casiOnlineRandomAdapter);
            try{
                tv_xemthem_album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavHostFragment.findNavController(Online.this)
                                .navigate(R.id.action_item_online_to_danhSachAlbumOnline2);
                    }
                });
                tv_xemthem_casi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavHostFragment.findNavController(Online.this)
                                .navigate(R.id.action_item_online_to_danhSachCaSiOnline);
                    }
                });
            }catch (Exception e){

            }
        }else {
            try {
                layalbumGoiy();
                laycasigoiy();
                tv_xemthem_album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavHostFragment.findNavController(Online.this)
                                .navigate(R.id.action_item_online_to_danhSachAlbumOnline2);
                    }
                });
                tv_xemthem_casi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavHostFragment.findNavController(Online.this)
                                .navigate(R.id.action_item_online_to_danhSachCaSiOnline);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void layalbumGoiy(){
        try {
            DataService dataService = APIService.getService();
            Call<List<Album>> listalbum = dataService.LaydulieualbumGoiy();
            listalbum.enqueue(new Callback<List<Album>>() {
                @Override
                public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                    albumarraylist = (ArrayList<Album>) response.body();
                    adapter = new AlbumOnlineRandomAdapter(getContext(), albumarraylist, getParentFragment());
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    manager.setOrientation(RecyclerView.HORIZONTAL);
                    rcv_album.setLayoutManager(manager);
                    rcv_album.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Album>> call, Throwable t) {

                }
            });
        }catch(Exception e){
            Toast.makeText(getActivity(), "Không tải được album", Toast.LENGTH_LONG).show();
        }
    }

    public void laycasigoiy(){
        try{
            DataService dataService = APIService.getService();
            Call<List<Casi>> listcasi = dataService.LaydulietcasiAll();
            listcasi.enqueue(new Callback<List<Casi>>() {
                @Override
                public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                    casiArrayList = (ArrayList<Casi>) response.body();
                    int length = casiArrayList.size();
                    Random r = new Random();
                    casiRamdom = new ArrayList<>();
                    for(int i = 0; i < 3; i ++){
                        int pos = r.nextInt(length - 0 + 1) + 0;
                        casiRamdom.add(casiArrayList.get(pos));
                    }
                    casiOnlineRandomAdapter = new CasiOnlineRandomAdapter(getContext(), casiRamdom, getParentFragment());
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    manager.setOrientation(RecyclerView.HORIZONTAL);
                    rcv_casi.setLayoutManager(manager);
                    rcv_casi.setAdapter(casiOnlineRandomAdapter);
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {

                }
            });
        }catch(Exception e){

        }
    }
}
