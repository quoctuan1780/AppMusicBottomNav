package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.AlbumOnlineAdapter;
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachAlbumOnline extends Fragment {
    private View view;
    private Toolbar toolbar;
    private ArrayList<Album> albumarraylist;
    private AlbumOnlineAdapter adapter;
    private ListView lv_album_online_all;
    ArrayList<Baihat> baihatarraylist;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album_online_all, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.tb_album_online_all);
        lv_album_online_all = (ListView) view.findViewById(R.id.lv_album_online_all);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Danh sách album");
        toolbar.setBackgroundColor(R.color.gray_color);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try{
            layalbumAll();
            laydanhsachbaihat();
        }catch (Exception e){

        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void laydanhsachbaihat(){
        lv_album_online_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album albumhientai = adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) albumhientai.getListBaiHat());
                NavHostFragment.findNavController(DanhSachAlbumOnline.this)
                        .navigate(R.id.action_danhSachAlbumOnline_to_danhsachbaihatOnline, bundle);
            }
        });
    }

    private void layalbumAll(){
        DataService dataService = APIService.getService();
        Call<List<Album>> listalbum = dataService.LaydulieuadbumAll();
        listalbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                try{
                    albumarraylist = (ArrayList<Album>) response.body();
                    adapter = new AlbumOnlineAdapter(getContext(), albumarraylist);
                    lv_album_online_all.setAdapter(adapter);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}
