package com.example.appmusicbotnav.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.PlayListSelectAdapter;
import com.example.appmusicbotnav.db.Database;
import com.example.appmusicbotnav.model.BaiHat;
import com.example.appmusicbotnav.model.PlayList;
import java.util.ArrayList;

public class PlayListOffline extends Fragment {
    private View view;
    private AlertDialog.Builder hopthoainhaptenplaylist;
    private EditText nhap;
    private Button taoplaylist;
    private ListView lv_baihat;
    private ArrayList<PlayList> playLists = new ArrayList<>();
    private Database database;
    public PlayListOffline(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_playlist_offline, container, false);
        database = new Database(getContext(), "music.sqlite", null, 1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        taoplaylist = (Button) view.findViewById(R.id.bt_tao_playlist);
        taoplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienthinhaptenplaylist();
            }
        });
        lv_baihat = (ListView) view.findViewById(R.id.lv_playlist_off);
        hienthiPlaylist();
        super.onViewCreated(view, savedInstanceState);
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
                if(nhap.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Tên playlist không được để trống", Toast.LENGTH_LONG).show();
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("tenplaylist", nhap.getText().toString());
                NavHostFragment.findNavController(PlayListOffline.this)
                        .navigate(R.id.action_playlist_to_chonbhplaylistoff, bundle);
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
            Cursor layplaylist = database.Laydulieu("SELECT * FROM Playlist");
            while (layplaylist.moveToNext()){
                ArrayList<BaiHat> listBh = new ArrayList<>();

                int id = layplaylist.getInt(0);
                String tenPlaylist = layplaylist.getString(1);
                Cursor laybaihat = database.Laydulieu("SELECT * FROM Baihatplaylist WHERE id = " + id);
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
            PlayListSelectAdapter adapter = new PlayListSelectAdapter(getContext(), playLists);
            lv_baihat.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Chưa có playlist", Toast.LENGTH_LONG).show();
        }

    }


}
