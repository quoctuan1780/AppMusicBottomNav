package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.PlayListSelectAdapter;
import com.example.appmusicbotnav.db.BaiHatDBOffline;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.BaiHat;
import com.example.appmusicbotnav.model.PlayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DanhSachPlayListOffline extends Fragment {
    private View view;
    private Toolbar toolbar;
    private PlaylistDBOffline playlistDBOffline;
    private BaiHatDBOffline baiHatDBOffline;
    private AlertDialog.Builder hopthoainhaptenplaylist;
    private EditText nhap;
    private Button taoplaylist;
    private ListView lv_baihat;
    private ArrayList<PlayList> playLists = new ArrayList<>();
    private PopupMenu PopMenuPlaylist;
    private PlayListSelectAdapter adapter;
    private PopupMenu dropMenu;
    private Menu menu;
    private ImageButton sort;
    private SearchView sv_timkiemplaylist;
    public DanhSachPlayListOffline(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_playlist);
        toolbar.setBackgroundColor(R.color.gray_color);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        playlistDBOffline = new PlaylistDBOffline(getContext(), "music.sqlite", null, 1);
        baiHatDBOffline = new BaiHatDBOffline(getContext(), "music.sqlite", null, 1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null){
            if(getArguments().getString("ok") != null && getArguments().getString("ok").equals("ok")) {
                Toast.makeText(getActivity(), "Sửa bài hát playlist thành công", Toast.LENGTH_LONG).show();
            }
            else if(getArguments().getInt("taoplthanhcong") == 1){
                Toast.makeText(getActivity(), "Tạo playlist thành công", Toast.LENGTH_LONG).show();
            }
        }
        taoplaylist = (Button) view.findViewById(R.id.bt_tao_playlist);
        taoplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienthinhaptenplaylist();
            }
        });
        lv_baihat = (ListView) view.findViewById(R.id.lv_playlist_off);
        sort = (ImageButton) view.findViewById(R.id.ib_sapxep_playlist_off);
        sv_timkiemplaylist = view.findViewById(R.id.sv_timkiemplaylist);
        hienthiPlaylist();
        chonplaylist();
        chonplaylistsuabaihat();
        khoitaomenusapxepplaylist();
        khoitaotimkiemplaylist();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_item_playlist, menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavHostFragment.findNavController(DanhSachPlayListOffline.this)
                    .navigate(R.id.action_frag_playlist_to_item_canhan2);
        }
        return super.onOptionsItemSelected(item);
    }

    private void khoitaotimkiemplaylist(){
        final ArrayList<PlayList> listClone = new ArrayList<>();
        sv_timkiemplaylist.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new PlayListSelectAdapter(getContext(), playLists);
                    lv_baihat.setAdapter(adapter);
                    return false;
                }
                else{
                    listClone.clear();
                    for(PlayList pl : playLists)
                        if(pl.getTenPlaylist().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(pl);
                        }
                    adapter = new PlayListSelectAdapter(getContext(), listClone);
                    lv_baihat.setAdapter(adapter);
                }
                return false;
            }
        });
    }
    private void khoitaomenuplaylist(View v){
        PopMenuPlaylist = new PopupMenu(getContext(), v);
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_item_playlist, PopMenuPlaylist.getMenu());
        PopMenuPlaylist.show();
    }

    private void hienthinhaptenplaylist(){
        hopthoainhaptenplaylist = new AlertDialog.Builder(getContext());
        hopthoainhaptenplaylist.setTitle("Nhập tên playlist");

        nhap = new EditText(getContext());
        nhap.setInputType(InputType.TYPE_CLASS_TEXT);
        hopthoainhaptenplaylist.setView(nhap);

        hopthoainhaptenplaylist.setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = playlistDBOffline.LayPlayList(nhap.getText().toString());
                if(cursor.moveToNext()){
                    Toast.makeText(getActivity(), "Playlist đã tồn tại", Toast.LENGTH_LONG).show();
                }
                else {
                    if (nhap.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Tên playlist không được để trống", Toast.LENGTH_LONG).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("tenplaylist", nhap.getText().toString());
                        NavHostFragment.findNavController(DanhSachPlayListOffline.this)
                                .navigate(R.id.action_playlist_to_chonbhplaylistoff, bundle);
                    }
                }
            }
        });

        hopthoainhaptenplaylist.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        hopthoainhaptenplaylist.show();
    }

    private void hienthiPlaylist(){
        try{
            Cursor layplaylist = playlistDBOffline.LayPlaylist();
            while (layplaylist.moveToNext()){
                ArrayList<BaiHat> listBh = new ArrayList<>();

                int id = layplaylist.getInt(0);
                String tenPlaylist = layplaylist.getString(1);
                Cursor laybaihat = baiHatDBOffline.LayBaiHatPlaylist(id);
                while (laybaihat.moveToNext()){
                    String tenbh = laybaihat.getString(2);
                    String tencs = laybaihat.getString(3);
                    String duongdan = laybaihat.getString(4);
                    BaiHat bh = new BaiHat(tenbh, tencs, duongdan);
                    listBh.add(bh);
                }
                PlayList playList = new PlayList(tenPlaylist, listBh);
                playLists.add(playList);
            }
            adapter = new PlayListSelectAdapter(getContext(), playLists);
            lv_baihat.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Chưa có playlist", Toast.LENGTH_LONG).show();
        }
    }

    private void suatenplaylist(final int pos){
        hopthoainhaptenplaylist = new AlertDialog.Builder(getContext());
        hopthoainhaptenplaylist.setTitle("Nhập tên playlist");

        nhap = new EditText(getContext());
        nhap.setInputType(InputType.TYPE_CLASS_TEXT);
        hopthoainhaptenplaylist.setView(nhap);

        hopthoainhaptenplaylist.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(nhap.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Tên playlist không được để trống", Toast.LENGTH_LONG).show();
                }
                else{
                    String tenplaylist = adapter.getItem(pos).getTenPlaylist();
                    Cursor layplaylist = playlistDBOffline.LayPlayList(tenplaylist);
                    if(layplaylist != null){
                        layplaylist.moveToNext();
                        int id = layplaylist.getInt(0);
                        playlistDBOffline.SuaTenPlaylist(id, nhap.getText().toString());
                        adapter.updateAdapter();
                    }
                }
            }
        });

        hopthoainhaptenplaylist.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        hopthoainhaptenplaylist.show();
    }

    private void suabaihattrongplaylist(int pos){
        String tenpl = adapter.getItem(pos).getTenPlaylist();
        Bundle bundle = new Bundle();
        bundle.putString("tenplaylist", tenpl);
        NavHostFragment.findNavController(DanhSachPlayListOffline.this)
                .navigate(R.id.action_frag_playlist_to_suaBaiHatTrongPlaylistOff, bundle);
    }

    private void chonplaylist(){
        lv_baihat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayList playlisthientai;
                playlisthientai = adapter.DanhSachPlaylist(adapter.getItem(position).getTenPlaylist());
                Bundle bundle = new Bundle();
                bundle.putSerializable("listbh", playlisthientai.getBaiHat());
                bundle.putInt("key", 1);
                NavHostFragment.findNavController(DanhSachPlayListOffline.this)
                        .navigate(R.id.action_frag_playlist_to_frag_dsbhoff, bundle);
            }
        });
    }

    private void chonplaylistsuabaihat() {
        lv_baihat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                khoitaomenuplaylist(view);
                PopMenuPlaylist.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_xoa_playlist:
                                String tenplaylist = adapter.getItem(position).getTenPlaylist();
                                Cursor layplaylist = playlistDBOffline.LayPlayList(tenplaylist);
                                if(layplaylist != null){
                                    layplaylist.moveToNext();
                                    int id = layplaylist.getInt(0);
                                    playlistDBOffline.XoaPlaylist(id);
                                    Toast.makeText(getContext(), "Đã xóa playlist", Toast.LENGTH_LONG).show();
                                    adapter.updateAdapter();
                                    return true;
                                }
                                break;
                            case R.id.item_sua_playlist:
                                suatenplaylist(position);
                                return true;
                            case R.id.item_sua_baihattrong_playlist:
                                suabaihattrongplaylist(position);
                                return true;
                        }
                        return false;
                    }
                });
                return true;
            }
        });
    }

    private void khoitaomenusapxepplaylist(){
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
        Collections.sort(playLists, new Comparator<PlayList>() {
            @Override
            public int compare(PlayList o1, PlayList o2) {
                return o1.getTenPlaylist().compareTo(o2.getTenPlaylist());
            }
        });
    }

    private void sapxepgiamdan(){
        Collections.sort(playLists, new Comparator<PlayList>() {
            @Override
            public int compare(PlayList o1, PlayList o2) {
                return o2.getTenPlaylist().compareTo(o1.getTenPlaylist());
            }
        });
    }
//    private void addTabs() {
//        tabLayout = (TabLayout) view.findViewById(R.id.tab_playlist);
//        pager = (ViewPager) view.findViewById(R.id.vp_playlist);
//        pager.setAdapter(new PlaylistTablayoutAdapter(getFragmentManager()));
//        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setupWithViewPager(pager);
//        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                pager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
}
