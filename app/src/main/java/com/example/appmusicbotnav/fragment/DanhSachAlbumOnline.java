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
import android.widget.SearchView;
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
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachAlbumOnline extends Fragment {
    private View view;
    private Toolbar toolbar;
    private static ArrayList<Album> albumarraylist;
    private AlbumOnlineAdapter adapter;
    private ListView lv_album_online_all;
    private ArrayList<Album> albumArrayListClone = new ArrayList<>();
    private SearchView sv_albumOnline;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album_online_all, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.tb_album_online_all);
        lv_album_online_all = (ListView) view.findViewById(R.id.lv_album_online_all);
        sv_albumOnline = (SearchView) view.findViewById(R.id.sv_timkiem_album_online);
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
        if(albumarraylist !=  null){
            adapter = new AlbumOnlineAdapter(getActivity(), albumarraylist);
            lv_album_online_all.setAdapter(adapter);
            timkiemalbum();
            laydanhsachbaihat();
        }else {
            try {
                layalbumAll();
                timkiemalbum();
                laydanhsachbaihat();
            } catch (Exception e) {

            }
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
        try{
            DataService dataService = APIService.getService();
            Call<List<Album>> listalbum = dataService.LaydulieuadbumAll();

            listalbum.enqueue(new Callback<List<Album>>() {
                @Override
                public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                    if(response.isSuccessful()) {
                        albumarraylist = (ArrayList<Album>) response.body();
                        if(getContext() != null) {
                            adapter = new AlbumOnlineAdapter(getContext(), albumarraylist);
                            lv_album_online_all.setAdapter(adapter);
                        }
                    }else{
                        Toast.makeText(getContext(), "Không tải được danh sách album", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Album>> call, Throwable t) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
        }
    }

    private void timkiemalbum() {
        sv_albumOnline.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(albumarraylist != null) {
                    if (newText.equals("")) {
                        albumArrayListClone.clear();
                        adapter = new AlbumOnlineAdapter(getActivity(), albumarraylist);
                        lv_album_online_all.setAdapter(adapter);
                        return false;
                    } else {
                        albumArrayListClone.clear();
                        for (Album al : albumarraylist) {
                            if (al.getTenAlbum().toLowerCase().contains(newText.toLowerCase())) {
                                albumArrayListClone.add(al);
                            }
                        }
                        adapter = new AlbumOnlineAdapter(getContext(), albumArrayListClone);
                        lv_album_online_all.setAdapter(adapter);
                        return false;
                    }
                }else{
                    Toast.makeText(getContext(), "Không có album để tìm kiếm", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });
    }
}
