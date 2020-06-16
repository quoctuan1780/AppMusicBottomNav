package com.example.appmusicbotnav.fragment;

import android.os.Bundle;
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
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Online extends Fragment  {
    private View view;
    private TextView tv_xemthem_album;
    private ArrayList<Album> albumarraylist;
    private RecyclerView rcv_album;
    private AlbumOnlineRandomAdapter adapter;

    public Online(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        tv_xemthem_album = (TextView) view.findViewById(R.id.tv_album_xemthem);
        rcv_album = (RecyclerView) view.findViewById(R.id.rcv_album_online);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            layalbumGoiy();
            tv_xemthem_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(Online.this)
                            .navigate(R.id.action_item_online_to_danhSachAlbumOnline2);
                }
            });
        }catch(Exception e){
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
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
}
