package com.example.appmusicbotnav.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.viewpager.widget.ViewPager;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.AlbumOnlineRandomAdapter;
import com.example.appmusicbotnav.adapter.BaiHatOnlineRandomAdapter;
import com.example.appmusicbotnav.adapter.CasiOnlineRandomAdapter;
import com.example.appmusicbotnav.adapter.PlaylistOnlineAdapter;
import com.example.appmusicbotnav.adapter.QuangCaoOnlineAdapter;
import com.example.appmusicbotnav.model.Slide;
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Casi;
import com.example.appmusicbotnav.modelOnline.Playlist;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Online extends Fragment  {
    private View view;
    private TextView tv_xemthem_album, tv_xemthem_casi;
    private static ArrayList<Casi> casiArrayList, casiRamdom;
    private static  ArrayList<Album> albumarraylist;
    private static ArrayList<Baihat> baihatArrayList;
    private static ArrayList<Playlist> playlistArrayList;
    private RecyclerView rcv_album, rcv_casi, rcv_baihat_goiy_online, rcv_playlist_online;
    private AlbumOnlineRandomAdapter adapter;
    private CasiOnlineRandomAdapter casiOnlineRandomAdapter;
    private BaiHatOnlineRandomAdapter baiHatOnlineAdapter;
    private PlaylistOnlineAdapter playlistOnlineAdapter;
    private ViewPager vp_quangcao;
    private QuangCaoOnlineAdapter slideAdapter;
    private ArrayList<Slide> slideArrayList = new ArrayList<>();
    private Runnable runnable;
    private Handler handler;
    private int quangCaoHienTai;
    private CircleIndicator ci_quangcao;
    private Session session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        tv_xemthem_album = (TextView) view.findViewById(R.id.tv_album_xemthem);
        tv_xemthem_casi = (TextView) view.findViewById(R.id.tv_casi_xemthem);
        rcv_album = (RecyclerView) view.findViewById(R.id.rcv_album_online);
        rcv_casi = (RecyclerView) view.findViewById(R.id.rcv_casi_online);
        rcv_baihat_goiy_online = (RecyclerView) view.findViewById(R.id.rcv_baihat_goiy_online);
        rcv_playlist_online = (RecyclerView) view.findViewById(R.id.rcv_playlist_online);
        vp_quangcao = (ViewPager) view.findViewById(R.id.vp_quangcao_online);
        ci_quangcao = (CircleIndicator) view.findViewById(R.id.ci_quangcao);
        session = new Session(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layHinhAnhQuangCao();
        if(getContext() != null && albumarraylist != null && casiArrayList != null && baihatArrayList != null
            && !albumarraylist.isEmpty() && !casiArrayList.isEmpty() && !baihatArrayList.isEmpty() && casiRamdom != null
        && !slideArrayList.isEmpty() &&playlistArrayList != null && playlistArrayList.size() != 0){
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            adapter = new AlbumOnlineRandomAdapter(getContext(), albumarraylist, getParentFragment());
            manager.setOrientation(RecyclerView.HORIZONTAL);
            rcv_album.setLayoutManager(manager);
            rcv_album.setAdapter(adapter);
            LinearLayoutManager casiManager = new LinearLayoutManager(getActivity());
            casiOnlineRandomAdapter = new CasiOnlineRandomAdapter(getContext(), casiRamdom, getParentFragment());
            casiManager.setOrientation(RecyclerView.HORIZONTAL);
            rcv_casi.setLayoutManager(casiManager);
            rcv_casi.setAdapter(casiOnlineRandomAdapter);
            baiHatOnlineAdapter = new BaiHatOnlineRandomAdapter(getContext(), baihatArrayList);
            LinearLayoutManager baihatManager = new LinearLayoutManager(getActivity());
            baihatManager.setOrientation(RecyclerView.VERTICAL);
            rcv_baihat_goiy_online.setLayoutManager(baihatManager);
            rcv_baihat_goiy_online.setAdapter(baiHatOnlineAdapter);

            playlistOnlineAdapter = new PlaylistOnlineAdapter(getContext(), playlistArrayList, getParentFragment());
            LinearLayoutManager playlistManager = new LinearLayoutManager(getActivity());
            playlistManager.setOrientation(RecyclerView.VERTICAL);
            rcv_playlist_online.setLayoutManager(playlistManager);
            rcv_playlist_online.setAdapter(playlistOnlineAdapter);
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
                laybaihatgoiy();
                if(session.getToken() != null){
                    layPlaylist();
                }
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

    private void layHinhAnhQuangCao(){
        if(slideArrayList.isEmpty()) {
            slideArrayList.add(new Slide(R.drawable.bander1));
            slideArrayList.add(new Slide(R.drawable.bander2));
            slideArrayList.add(new Slide(R.drawable.bander3));
            slideArrayList.add(new Slide(R.drawable.bander4));
        }
        slideAdapter = new QuangCaoOnlineAdapter(getActivity(), slideArrayList);
        vp_quangcao.setAdapter(slideAdapter);
        ci_quangcao.setViewPager(vp_quangcao);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                quangCaoHienTai = vp_quangcao.getCurrentItem();
                quangCaoHienTai++;
                if(quangCaoHienTai >= vp_quangcao.getAdapter().getCount()){
                    quangCaoHienTai = 0;
                }
                vp_quangcao.setCurrentItem(quangCaoHienTai, true);
                handler.postDelayed(runnable, 4500);
            }
        };
        handler.postDelayed(runnable, 4500);
    }

    public void layalbumGoiy(){
        try {
            DataService dataService = APIService.getService();
            Call<List<Album>> listalbum = dataService.LaydulieualbumGoiy();
            listalbum.enqueue(new Callback<List<Album>>() {
                @Override
                public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                    if(response.isSuccessful()) {
                        albumarraylist = (ArrayList<Album>) response.body();
                        if(getContext() != null && albumarraylist != null) {
                            adapter = new AlbumOnlineRandomAdapter(getContext(), albumarraylist, getParentFragment());
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            manager.setOrientation(RecyclerView.HORIZONTAL);
                            rcv_album.setLayoutManager(manager);
                            rcv_album.setAdapter(adapter);
                        }
                    }else{
                        Toast.makeText(getContext(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Album>> call, Throwable t) {
                    call.cancel();
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
                    if(response.isSuccessful()) {
                        casiArrayList = (ArrayList<Casi>) response.body();
                        if(getContext() != null && casiArrayList != null && casiArrayList.size() != 0) {
                            int length = casiArrayList.size();
                            Random r = new Random();
                            casiRamdom = new ArrayList<>();
                            for (int i = 0; i < 3; i++) {
                                int pos = r.nextInt(length - 0 + 1) + 0;
                                casiRamdom.add(casiArrayList.get(pos));
                            }
                            casiOnlineRandomAdapter = new CasiOnlineRandomAdapter(getContext(), casiRamdom, getParentFragment());
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            manager.setOrientation(RecyclerView.HORIZONTAL);
                            rcv_casi.setLayoutManager(manager);
                            rcv_casi.setAdapter(casiOnlineRandomAdapter);
                        }
                    }else{
                        Toast.makeText(getContext(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {
                    call.cancel();
                }
            });
        }catch(Exception e){

        }
    }

    private void laybaihatgoiy(){
        try{
            DataService dataService = APIService.getService();
            final Call<List<Baihat>> baihatCall =  dataService.laybaihatGoiY();
            baihatCall.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    if(response.isSuccessful()){
                        baihatArrayList = (ArrayList<Baihat>) response.body();
                        if(getContext() != null && baihatArrayList != null){
                            baiHatOnlineAdapter = new BaiHatOnlineRandomAdapter(getContext(), baihatArrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rcv_baihat_goiy_online.setLayoutManager(manager);
                            rcv_baihat_goiy_online.setAdapter(baiHatOnlineAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {
                    call.cancel();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void layPlaylist(){
        try{
            DataService dataService = APIService.getServicePlaylist();
            String token = session.getToken();
            String content = "Bearer " + token;
            Call<List<Playlist>> callPlaylist = dataService.LayPlaylistTheotaikhoan(content, session.getId());
            callPlaylist.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                    if(response.isSuccessful()){
                        playlistArrayList = (ArrayList<Playlist>) response.body();
                        if(getContext() != null && playlistArrayList.size() != 0) {
                            playlistOnlineAdapter = new PlaylistOnlineAdapter(getContext(), playlistArrayList, getParentFragment());
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rcv_playlist_online.setLayoutManager(manager);
                            rcv_playlist_online.setAdapter(playlistOnlineAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Playlist>> call, Throwable t) {

                }
            });
        }catch(Exception e){

        }
    }
}
