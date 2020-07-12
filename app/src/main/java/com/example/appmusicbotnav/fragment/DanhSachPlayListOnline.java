package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
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
import androidx.navigation.fragment.NavHostFragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.PlaylistOnlineAllAdapter;
import com.example.appmusicbotnav.modelOnline.BaiHat_Playlist;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Playlist;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.session.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachPlayListOnline extends Fragment {
    private View view;
    private Toolbar tb_playlist;
    private Session session;
    public static ArrayList<Playlist> playlistArrayList;
    private PlaylistOnlineAllAdapter adapter;
    private ListView lv_playlist;
    private Button bt_tao_playlist;
    private AlertDialog.Builder hopthoainhaptenplaylist;
    private EditText nhap;
    private ArrayList<Baihat> baihatArrayList = new ArrayList<>();
    private PopupMenu PopMenuPlaylist;
    private String token, content;
    private RelativeLayout relativeLayout;
    private android.widget.SearchView searchView;
    private PopupMenu dropMenu;
    private Menu menu;
    private ImageButton sort;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist_online, container, false);
        tb_playlist = (Toolbar) view.findViewById(R.id.tb_playlist_online);
        lv_playlist = (ListView) view.findViewById(R.id.lv_playlist_online);
        bt_tao_playlist = (Button) view.findViewById(R.id.bt_tao_playlist_online);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_playlist_online);
        searchView = (android.widget.SearchView) view.findViewById(R.id.sv_timkiemplaylist_online);
        sort = (ImageButton) view.findViewById(R.id.ib_sapxep_playlist_online);
        setHasOptionsMenu(true);
        tb_playlist.setBackgroundColor(R.color.gray_color);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_playlist);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
        bt_tao_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiNhapTenPlaylist();
            }
        });
        session = new Session(getContext());
        token = session.getToken();
        content = "Bearer " + token;
        if(playlistArrayList != null && playlistArrayList.size() > 0){
            adapter = new PlaylistOnlineAllAdapter(getContext(), playlistArrayList);
            lv_playlist.setAdapter(adapter);
        }else layPlaylistAll();
        layBaiHatPlaylist();
        menuPlaylist();
        khoiTaoMenuSapXepPlaylist();
        khoitaotimkiemplaylist();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Online.playlistArrayList = playlistArrayList;
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void layBaiHatPlaylist() {
        lv_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Baihat> baihats = new ArrayList<>();
                for (int i = 0; i < adapter.getItem(position).getListBaiHatPlayList().size(); i++) {
                    baihats.add(adapter.getItem(position).getListBaiHatPlayList().get(i).getBaiHat());
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) baihats);
                NavHostFragment.findNavController(DanhSachPlayListOnline.this)
                        .navigate(R.id.action_danhSachPlayListOnline_to_danhsachbaihatOnline, bundle);
            }
        });
    }

    private void layPlaylistAll(){
        try {
            DataService dataService = APIService.getServicePlaylist();
            Call<List<Playlist>> listCall = dataService.LayPlaylistTheotaikhoan(content, session.getId());
            listCall.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                    if (response.isSuccessful()) {
                        playlistArrayList = (ArrayList<Playlist>) response.body();
                        if (playlistArrayList != null && playlistArrayList.size() != 0) {
                            adapter = new PlaylistOnlineAllAdapter(getContext(), playlistArrayList);
                            lv_playlist.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Playlist>> call, Throwable t) {

                }
            });
        }catch (Exception e){

        }
    }

    private void hienThiNhapTenPlaylist(){
        hopthoainhaptenplaylist = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialog_custom = layoutInflater.inflate(R.layout.custom_dialog_tao_playlist, null);
        hopthoainhaptenplaylist.setView(dialog_custom);
        nhap = (EditText) dialog_custom.findViewById(R.id.et_tenplaylist);
        Button bt_tao = (Button) dialog_custom.findViewById(R.id.bt_tao_playlist_dialog);
        Button bt_huy = (Button) dialog_custom.findViewById(R.id.bt_huy_tao_playlist_dialog);
        final AlertDialog dialog = hopthoainhaptenplaylist.create();
        dialog.show();
        bt_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (nhap.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Tên playlist không được để trống", Toast.LENGTH_LONG).show();
            } else {
                DataService dataService = APIService.getServicePlaylist();
                String token = session.getToken();
                String content = "Bearer " + token;
                final Playlist playlist = new Playlist();
                playlist.setIdNguoiDung(session.getId());
                playlist.setTenPlayList((nhap.getText().toString()));
                playlist.setListBaiHatPlayList(new ArrayList<BaiHat_Playlist>());
                Call<Playlist> playlistCall = dataService.themPlaylist(content, playlist);
                playlistCall.enqueue(new Callback<Playlist>() {
                    @Override
                    public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Tạo playlist thành công", Toast.LENGTH_LONG).show();
                            playlistArrayList.add(response.body());
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("idPlaylist", response.body().getIdPlayList().toString());
                            NavHostFragment.findNavController(DanhSachPlayListOnline.this)
                                    .navigate(R.id.action_danhSachPlayListOnline_to_chonBaiHatPlaylistOnline, bundle);
                        }
                    }

                    @Override
                    public void onFailure(Call<Playlist> call, Throwable t) {

                    }
                });

            }
            }
        });

        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        if(playlistArrayList != null &&  playlistArrayList.size() > 0){
            adapter.updatePlaylist(playlistArrayList);
            Online.playlistArrayList = playlistArrayList;
        }
    }

    private void khoitaomenuplaylist(View v){
        PopMenuPlaylist = new PopupMenu(getContext(), v);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_item_playlist, PopMenuPlaylist.getMenu());
        PopMenuPlaylist.show();
    }

    private void xoaPlaylist(int position){
        String token = session.getToken();
        final String content = "Bearer " + token;
        DataService dataService = APIService.getServicePlaylist();

        dataService.xoaPlaylist(content, adapter.getItem(position).getIdPlayList()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(getContext(), "Xóa playlist thành công", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        Playlist playlist = adapter.getItem(position);
        playlistArrayList.remove(playlist);
        adapter.remove(playlist);
        adapter.notifyDataSetChanged();
        Online.playlistArrayList = playlistArrayList;
    }

    private void suaTenPlaylist(final int pos){
        hopthoainhaptenplaylist = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogcustom = layoutInflater.inflate(R.layout.custom_dialog_tao_playlist, null);

        hopthoainhaptenplaylist.setView(dialogcustom);
        nhap = (EditText) dialogcustom.findViewById(R.id.et_tenplaylist);
        Button bt_tao = (Button) dialogcustom.findViewById(R.id.bt_tao_playlist_dialog);
        Button bt_huy = (Button) dialogcustom.findViewById(R.id.bt_huy_tao_playlist_dialog);
        bt_tao.setText("SỬA");
        final AlertDialog dialog = hopthoainhaptenplaylist.create();
        dialog.show();
        bt_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nhap.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Tên playlist không được để trống", Toast.LENGTH_LONG).show();
                }
                else{
                    final Playlist playlist = playlistArrayList.get(pos);
                    playlist.setTenPlayList(nhap.getText().toString());
                    DataService dataService = APIService.getServicePlaylist();
                    Call<Playlist> playlistCall = dataService.suaTenPlaylist(content, playlist);
                    playlistCall.enqueue(new Callback<Playlist>() {
                        @Override
                        public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getContext(), "Sửa tên playlist thành công", Toast.LENGTH_LONG).show();
                                playlistArrayList.get(pos).setTenPlayList(nhap.getText().toString());
                                adapter.updatePlaylist(playlistArrayList);
                                Online.playlistArrayList = playlistArrayList;
                            }
                        }

                        @Override
                        public void onFailure(Call<Playlist> call, Throwable t) {

                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void suaBaiHatTrongPlaylist(int pos){
        Bundle bundle = new Bundle();
        bundle.putString("playlistId", playlistArrayList.get(pos).getIdPlayList().toString());
        bundle.putInt("vitri", pos);
        NavHostFragment.findNavController(DanhSachPlayListOnline.this)
                .navigate(R.id.action_danhSachPlayListOnline_to_chonBaiHatPlaylistOnline, bundle);
    }

    private void menuPlaylist(){
        lv_playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                khoitaomenuplaylist(view);
                PopMenuPlaylist.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_xoa_playlist:
                                xoaPlaylist(position);
                                break;
                            case R.id.item_sua_playlist:
                                suaTenPlaylist(position);
                                break;
                            case R.id.item_sua_baihattrong_playlist:
                                suaBaiHatTrongPlaylist(position);
                                break;
                        }
                        return false;
                    }
                });
                return true;
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void khoitaotimkiemplaylist(){
        final ArrayList<Playlist> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new PlaylistOnlineAllAdapter(getContext(), playlistArrayList);
                    lv_playlist.setAdapter(adapter);
                    return false;
                }
                else{
                    listClone.clear();
                    for(Playlist pl : playlistArrayList)
                        if(pl.getTenPlayList().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(pl);
                        }
                    adapter = new PlaylistOnlineAllAdapter(getContext(), listClone);
                    lv_playlist.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    private void khoiTaoMenuSapXepPlaylist(){
        dropMenu = new PopupMenu(getContext(), sort);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_sort, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_tangdan:
                        sapxeptangdan();
                        adapter.notifyDataSetChanged();
                        return true;
                    case R.id.item_giamdan:
                        sapxepgiamdan();
                        adapter.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropMenu.show();
            }
        });
    }

    private void sapxeptangdan(){
        Collections.sort(playlistArrayList, new Comparator<Playlist>() {
            @Override
            public int compare(Playlist o1, Playlist o2) {
                return o1.getTenPlayList().compareTo(o2.getTenPlayList());
            }
        });
    }

    private void sapxepgiamdan(){
        Collections.sort(playlistArrayList, new Comparator<Playlist>() {
            @Override
            public int compare(Playlist o1, Playlist o2) {
                return o2.getTenPlayList().compareTo(o1.getTenPlayList());
            }
        });
    }
}
