package com.example.appmusicbotnav.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.SlideAdapter;
import com.example.appmusicbotnav.db.PlaylistDBOffline;
import com.example.appmusicbotnav.model.Slide;
import java.util.ArrayList;

public class ThuVien extends Fragment {
    private View view;
    private Toolbar toolbar;
    private ArrayList<Slide> listQuangcao;
    private ViewPager vpQuangcao;
    private int soluongbh = 0, soluongpl = 0;
    private final int MY_PERMISSION_REQUEST = 1;
    private PlaylistDBOffline playlistDBOffline;
    public ThuVien() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        playlistDBOffline = new PlaylistDBOffline(getContext(), "music.sqlite", null, 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thuvien, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_thuvien);
        toolbar.setTitle("Thư viện");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        QuangCao();
        HienThiQuangCao();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tv_baihat = (TextView) view.findViewById(R.id.tv_sl_baihat);
        TextView tv_playlist = (TextView) view.findViewById(R.id.tv_sl_playlist);
        khoitaoquyentruycap();
        soluongbh = demsoluongbaihat();
        tv_baihat.setText(soluongbh+"");
        Cursor cursor = playlistDBOffline.DemSoLuongPlaylist();
        if(cursor.moveToNext()){
            soluongpl = Integer.parseInt(cursor.getString(0));
            tv_playlist.setText(soluongpl+"");
        }
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ll_baihat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_thuvien_to_dsbh);
            }
        });

        view.findViewById(R.id.ll_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_thuvien_playlist);
            }
        });
    }

    private void QuangCao() {
        listQuangcao = new ArrayList<Slide>();
        listQuangcao.add(new Slide(R.drawable.quangcao));
        listQuangcao.add(new Slide(R.drawable.quangcao));
    }

    private void HienThiQuangCao() {
        vpQuangcao = (ViewPager) view.findViewById(R.id.vp_quangcao);
        SlideAdapter adapter = new SlideAdapter(view.getContext(), listQuangcao);
        vpQuangcao.setAdapter(adapter);
    }

    //Phần khởi tạo bài hát từ bộ nhớ máy
    private void khoitaoquyentruycap(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }else{
            Log.i("TAG", "Da khoi tao quyen truy cap: ");
        }
    }

    private int demsoluongbaihat(){
        int sl = 0;
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(uri, null, null, null, null);
        if(songCursor != null && songCursor.moveToFirst()){
            do{
                sl++;
            }  while(songCursor.moveToNext());
        }
        songCursor.close();
        return sl;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permision Granted", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Permision not grant", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                }
        }
    }
}
