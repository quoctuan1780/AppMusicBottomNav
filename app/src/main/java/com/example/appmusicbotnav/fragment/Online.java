package com.example.appmusicbotnav.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatOnlineAdapter;
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Online extends Fragment  {
    private View view;
    private BaiHatOnlineAdapter adapter;
    private ListView lv_baihat;
    private ArrayList<Baihat> baihatarraylist;
    private HorizontalScrollView hscv_album;
    private TextView tv_xemthem_album;
    private ArrayList<Album> albumarraylist;
    public Online(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        lv_baihat = (ListView) view.findViewById(R.id.lv_baihat_online);
        hscv_album = (HorizontalScrollView) view.findViewById(R.id.hscv_album_online);
        tv_xemthem_album = (TextView) view.findViewById(R.id.tv_album_xemthem);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            layalbumAll();
            laybaibatAll();
            phatnhac();
        }catch(Exception e){
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
        }
    }

    public void phatnhac(){
        lv_baihat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) baihatarraylist);
                try {
                    startActivity(phatnhac);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void layalbumAll(){
        DataService dataService = APIService.getService();
        Call<List<Album>> listalbum = dataService.LaydulieuadbumAll();
        listalbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                try {
                    albumarraylist = (ArrayList<Album>) response.body();
                    LinearLayout viewgroup = new LinearLayout(getActivity());
                    viewgroup.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(300, 200);
                    layout.setMargins(10, 20, 10, 20);
                    for (int i = 0; i < albumarraylist.size(); i++) {
                        CardView cardView = new CardView(getActivity());
                        cardView.setRadius(10);
                        ImageView imageView = new ImageView(getActivity());
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (albumarraylist.get(i).getTenAlbum() != null) {
                            Picasso.with(getActivity()).load(R.drawable.miku).into(imageView);
                        }
                        cardView.setLayoutParams(layout);
                        cardView.addView(imageView);
                        viewgroup.addView(cardView);
                    }
                    hscv_album.addView(viewgroup);
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Không tải được album", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

    public void laybaibatAll(){
        DataService dataService = APIService.getService();
        Call<List<Baihat>> listbaihat = dataService.LaydulieubaihatAll();
        listbaihat.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                try {
                    baihatarraylist = (ArrayList<Baihat>) response.body();
                    adapter = new BaiHatOnlineAdapter(getContext(), baihatarraylist);
                    lv_baihat.setAdapter(adapter);
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }
}
