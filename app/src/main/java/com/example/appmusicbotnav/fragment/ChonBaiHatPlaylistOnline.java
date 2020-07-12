package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.BaiHatOnlineAdapter;
import com.example.appmusicbotnav.modelOnline.BaiHat_Playlist;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Playlist;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.session.Session;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ChonBaiHatPlaylistOnline extends Fragment {
    private View view;
    private Toolbar tb_chonbaihat;
    private static ArrayList<Baihat> baihatArrayList;
    private BaiHatOnlineAdapter adapter, baihatPlaylistAdapter;
    private ListView lv_chonbaihat, lv_baihat_playlist;
    private ArrayList<Baihat> baihatPlaylistArraylist = new ArrayList<>();
    private ArrayList<BaiHat_Playlist> baiHat_playlistArrayList = new ArrayList<>(), baiHat_playlistArrayListSua = new ArrayList<>();;
    private Session session;
    private SearchView sv_timkiembaihat;
    private String idPlaylist = null, playlistSuaId = null;
    private String token, content;
    private Playlist playlistSua;
    private int vitri = -1;
    private PopupMenu PopMenuPlaylist;
    private LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && !getArguments().isEmpty()) {
            idPlaylist = getArguments().getString("idPlaylist");
            playlistSuaId = getArguments().getString("playlistId");
            vitri = getArguments().getInt("vitri");
        }
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chonbaihat_playlist_online, container, false);
        tb_chonbaihat = (Toolbar) view.findViewById(R.id.tb_playlist_online);
        lv_chonbaihat = (ListView) view.findViewById(R.id.lv_chonbaihat_playlist_online);
        lv_baihat_playlist = (ListView) view.findViewById(R.id.lv_baihat_playlist_online);
        baihatPlaylistAdapter = new BaiHatOnlineAdapter(getContext(), baihatPlaylistArraylist);
        sv_timkiembaihat = (SearchView) view.findViewById(R.id.sv_timkiem_baihat_playlist_online);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll_chonbaihatplaylist_online);
        lv_baihat_playlist.setAdapter(baihatPlaylistAdapter);
        setHasOptionsMenu(true);
        tb_chonbaihat.setBackgroundColor(R.color.gray_color);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_chonbaihat);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
        session = new Session(getContext());
        token = session.getToken();
        content = "Bearer " + token;
        timKiemBaiHat();
        if(idPlaylist != null) {
            themBaiHatVaoPlaylist();
            xoaBaiHatPlaylist();
        }else {
            chinhSuaPlaylist(Integer.parseInt(playlistSuaId));
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_suabhplaylist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void khoiTaoMenuXoaBaiHat(View v){
        PopMenuPlaylist = new PopupMenu(getContext(), v);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_item_xoa, PopMenuPlaylist.getMenu());
        PopMenuPlaylist.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }else
            if(item.getItemId() == R.id.item_luubh_suaplaylist) {
                if (idPlaylist != null) {
                    DataService dataService = APIService.getServicePlaylist();
                    if (baiHat_playlistArrayList.size() > 0) {
                        Call<List<BaiHat_Playlist>> baiHat_playlistCall = dataService.thembaihatvaoPlaylist(content, baiHat_playlistArrayList);
                        DanhSachPlayListOnline.playlistArrayList.get(DanhSachPlayListOnline.playlistArrayList.size() - 1).setListBaiHatPlayList(baiHat_playlistArrayList);
                        baiHat_playlistCall.enqueue(new Callback<List<BaiHat_Playlist>>() {
                            @Override
                            public void onResponse(Call<List<BaiHat_Playlist>> call, Response<List<BaiHat_Playlist>> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Thêm bài hát vào playlist thành công", Toast.LENGTH_LONG).show();
                                    getActivity().onBackPressed();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<BaiHat_Playlist>> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Không có bài hát nào được chọn", Toast.LENGTH_LONG).show();
                    }
                }
                else if(playlistSuaId != null) {
                    DataService dataService = APIService.getServicePlaylist();
                    Call<List<BaiHat_Playlist>> baiHat_playlistCall = dataService.thembaihatvaoPlaylist(content, baiHat_playlistArrayListSua);
                    for(int i = 0; i < baiHat_playlistArrayListSua.size(); i++){
                        baiHat_playlistArrayList.add(baiHat_playlistArrayListSua.get(i));
                    }
                    baiHat_playlistCall.enqueue(new Callback<List<BaiHat_Playlist>>() {
                        @Override
                        public void onResponse(Call<List<BaiHat_Playlist>> call, Response<List<BaiHat_Playlist>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Sửa bài hát vào playlist thành công", Toast.LENGTH_LONG).show();
                                DanhSachPlayListOnline.playlistArrayList.get(vitri).setListBaiHatPlayList(baiHat_playlistArrayList);
                                getActivity().onBackPressed();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BaiHat_Playlist>> call, Throwable t) {

                        }
                    });
                }
            }
        return super.onOptionsItemSelected(item);
    }

    private void timKiemBaiHat(){
        sv_timkiembaihat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query.replace('đ', 'd');
                DataService dataService = APIService.getService();
                Call<List<Baihat>> listCall = dataService.timkiembaihat(removeAccent(query));
                listCall.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        if(response.isSuccessful()){
                            baihatArrayList = (ArrayList<Baihat>) response.body();
                            if (baihatArrayList != null && baihatArrayList.size() != 0) {
                                adapter = new BaiHatOnlineAdapter(getContext(), baihatArrayList);
                                lv_chonbaihat.setAdapter(adapter);
                            }else{
                                Toast.makeText(getContext(), "Không tìm thấy bài hát nào", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    private void xoaBaiHatPlaylist(){
        lv_baihat_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baihatPlaylistArraylist.remove(position);
                baihatPlaylistAdapter.updateBaihat(baihatPlaylistArraylist);
                baiHat_playlistArrayList.remove(position);
            }
        });
    }

    private void chinhSuaPlaylist(final int idPlaylist){
        final DataService dataService = APIService.getServicePlaylist();
        final Call<Playlist> playlistCall = dataService.LayPlaylist(content, idPlaylist);
        playlistCall.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(response.isSuccessful()){
                    playlistSua = response.body();
                    baiHat_playlistArrayList = (ArrayList<BaiHat_Playlist>) playlistSua.getListBaiHatPlayList();
                    baihatPlaylistArraylist = new ArrayList<>();
                    final ArrayList<Baihat> baihats = new ArrayList<>();
                    for(int i = 0; i < baiHat_playlistArrayList.size(); i++){
                        baihatPlaylistArraylist.add(baiHat_playlistArrayList.get(i).getBaiHat());
                        baihats.add(baiHat_playlistArrayList.get(i).getBaiHat());
                    }
                    baihatPlaylistAdapter = new BaiHatOnlineAdapter(getContext(), baihatPlaylistArraylist);
                    lv_baihat_playlist.setAdapter(baihatPlaylistAdapter);
                    lv_baihat_playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            khoiTaoMenuXoaBaiHat(view);
                            final int pos = position;

                            PopMenuPlaylist.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch ((item.getItemId())){
                                        case R.id.item_xoa_playlist:
                                            if(!(baihats.indexOf(baihatPlaylistAdapter.getItem(pos)) == pos)){
                                                Toast.makeText(getContext(), "Bài hát này chưa thêm vào Playlist, chạm 1 cái để loại bỏ", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                baihats.remove(pos);
                                                baiHat_playlistArrayList.remove(pos);
                                                DanhSachPlayListOnline.playlistArrayList.get(vitri).setListBaiHatPlayList(baiHat_playlistArrayList);
                                                xoaMotBaiHatOnline(content, idPlaylist, baihatPlaylistAdapter.getItem(pos).getIdBaiHat());
                                                baihatPlaylistAdapter.updateBaihat(baihats);
                                            }
                                            break;
                                    }
                                    return false;
                                }
                            });

                            return false;
                        }
                    });

                    themBaiHatVaoPlaylistSua(idPlaylist, baihatPlaylistAdapter, baihatPlaylistArraylist, baiHat_playlistArrayListSua);
                    xoaBaiHatKhoiPlaylistSua(baihats, baihatPlaylistArraylist, baihatPlaylistAdapter, baiHat_playlistArrayListSua);
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {

            }
        });
    }

    private void xoaMotBaiHatOnline(String content, int idPlaylist, int idBaihat){
        DataService dataService = APIService.getServicePlaylist();
        dataService.xoaMotBaiHatPlaylist(content, idPlaylist, idBaihat).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(),"Đã xóa", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void themBaiHatVaoPlaylistSua(final int idPlaylist, final BaiHatOnlineAdapter baihatPlaylistAdapter, final ArrayList<Baihat> baihatPlaylistArraylist, final ArrayList<BaiHat_Playlist> baiHat_playlistArrayList){
        lv_chonbaihat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baihatPlaylistArraylist.add(baihatArrayList.get(position));
                baihatPlaylistAdapter.updateBaihat(baihatPlaylistArraylist);
                BaiHat_Playlist baiHatPlaylist = new BaiHat_Playlist();
                baiHatPlaylist.setIdPlayList(idPlaylist);
                baiHatPlaylist.setIdBaiHat(baihatArrayList.get(position).getIdBaiHat());
                baiHatPlaylist.setBaiHat(baihatArrayList.get(position));
                baiHat_playlistArrayList.add(baiHatPlaylist);
            }
        });
    }

    private void xoaBaiHatKhoiPlaylistSua(final ArrayList<Baihat> baihats, final ArrayList<Baihat> baihatPlaylistArraylist, final BaiHatOnlineAdapter baihatPlaylistAdapter, final ArrayList<BaiHat_Playlist> baiHat_playlistArrayList){
        lv_baihat_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!baihats.contains(baihatPlaylistArraylist.get(position))) {
                    baihatPlaylistArraylist.remove(position);
                    baihatPlaylistAdapter.updateBaihat(baihatPlaylistArraylist);
                    try {
                        baiHat_playlistArrayList.remove(position - baihats.size());
                    }catch (Exception e){

                    }
                }else{
                    Log.i("TAG", "Bạn cần nhấn giữ để loại bỏ bài hát này khỏi Playlist");
                }
            }
        });
    }

    private void themBaiHatVaoPlaylist(){
        if(Integer.parseInt(idPlaylist) != 0) {
            lv_chonbaihat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    baihatPlaylistArraylist.add(baihatArrayList.get(position));
                    baihatPlaylistAdapter.updateBaihat(baihatPlaylistArraylist);
                    BaiHat_Playlist baiHatPlaylist = new BaiHat_Playlist();
                    baiHatPlaylist.setIdPlayList(Integer.parseInt(idPlaylist));
                    baiHatPlaylist.setIdBaiHat(baihatArrayList.get(position).getIdBaiHat());
                    baiHatPlaylist.setBaiHat(baihatArrayList.get(position));
                    baiHat_playlistArrayList.add(baiHatPlaylist);
                }
            });
        }else{
            Toast.makeText(getContext(), "Không có playlist này, vui lòng tạo lại", Toast.LENGTH_LONG).show();
        }
    }
}
