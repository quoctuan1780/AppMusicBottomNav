package com.example.appmusicbotnav.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appmusicbotnav.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    private Animation diaxoay;
    private BottomNavigationView nav_thuvien;
    private NavController nav_con_thuvien;
    private ImageView ib_play_main, ib_baiketiep_index, ib_baitruoc_index, iv_disk_index;
    private TextView tv_tenbaihat_index, tv_tencasi_index;
    private LinearLayout ll_thanhdieukhiennhac_index;
    private int countClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            countClick = 0;
            nav_thuvien = (BottomNavigationView) findViewById(R.id.nav_thuvien);
            nav_con_thuvien = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(nav_thuvien, nav_con_thuvien);
            diaxoay = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);
            anhxa();
            playnhac();
            baiketiep();
            baitruocdo();

            ll_thanhdieukhiennhac_index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countClick++;
                    if (countClick == 1) {
                        countClick = 0;
                        Intent intent = new Intent(MainActivity.this, PhatNhac.class);
                        if(PhatNhac.listBaihat != null) {
                            intent.putExtra("vitri", PhatNhac.vitribai);
                            intent.putExtra("list", (Serializable) PhatNhac.listBaihat);
                            startActivity(intent);
                        }
                        else if(PhatNhac.listBaihatOnline != null){
                            intent.putExtra("vitri", PhatNhac.vitribai);
                            intent.putExtra("listonline", (Serializable) PhatNhac.listBaihatOnline);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Bạn đang nhấn quá nhanh", Toast.LENGTH_LONG).show();
                        countClick = 0;
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Có lỗi trong quá trình đọc danh sách bài hát đang phát", Toast.LENGTH_LONG).show();
        }
    }

    private void anhxa(){
        ib_play_main = (ImageView) findViewById(R.id.ib_choinhac_index);
        ib_baiketiep_index = (ImageView) findViewById(R.id.ib_baiketiep_index);
        ib_baitruoc_index = (ImageView) findViewById(R.id.ib_baitruoc_index);
        iv_disk_index = (ImageView) findViewById(R.id.iv_disk_index);
        tv_tenbaihat_index = (TextView) findViewById(R.id.tv_tenbaihat_index);
        tv_tencasi_index = (TextView) findViewById(R.id.tv_tencasi_index);
        ll_thanhdieukhiennhac_index = (LinearLayout) findViewById(R.id.ll_thanhdieukhiennhac_index1);
    }
    private void playnhac(){
        ib_play_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhatNhac.choinhac != null){
                    if(PhatNhac.choinhac.isPlaying()) {
                        iv_disk_index.clearAnimation();
                        PhatNhac.choinhac.pause();
                        ib_play_main.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    else{
                        PhatNhac.choinhac.start();
                        iv_disk_index.startAnimation(diaxoay);
                        ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                }
            }
        });
    }

    private void baiketiep(){
        ib_baiketiep_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhatNhac.choinhac != null) {
                    PhatNhac.vitribai++;
                    if(PhatNhac.listBaihat != null) {
                        if (PhatNhac.vitribai > PhatNhac.listBaihat.size() - 1) {
                            PhatNhac.vitribai = 0;
                        }
                    }else if(PhatNhac.listBaihatOnline != null){
                        if (PhatNhac.vitribai > PhatNhac.listBaihatOnline.size() - 1) {
                            PhatNhac.vitribai = 0;
                        }
                    }
                    PhatNhac.choinhac.stop();
                    PhatNhac.choinhac.release();
                    PhatNhac.choinhac = new MediaPlayer();
                    try{
                        if(PhatNhac.listBaihat != null) {
                            PhatNhac.choinhac.setDataSource(PhatNhac.listBaihat.get(PhatNhac.vitribai).getPath());
                            PhatNhac.choinhac.prepare();
                            PhatNhac.tenbh = PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle();
                            PhatNhac.tencs = PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle();
                            PhatNhac.tenbhcu = PhatNhac.tenbh;
                            tv_tenbaihat_index.setText(PhatNhac.tenbh);
                            tv_tencasi_index.setText(PhatNhac.tencs);
                            PhatNhac.choinhac.start();
                            iv_disk_index.startAnimation(diaxoay);
                            ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
                        }
                        else if(PhatNhac.listBaihatOnline != null){
                            new PlayMusicSync(MainActivity.this).execute();
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    private void baitruocdo(){
        ib_baitruoc_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhatNhac.choinhac != null) {
                    PhatNhac.vitribai--;
                    if(PhatNhac.listBaihat != null) {
                        if (PhatNhac.vitribai < 0) {
                            PhatNhac.vitribai = PhatNhac.listBaihat.size() - 1;
                        }
                    }else if(PhatNhac.listBaihatOnline != null){
                        if (PhatNhac.vitribai < 0) {
                            PhatNhac.vitribai = PhatNhac.listBaihatOnline.size() - 1;
                        }
                    }
                    PhatNhac.choinhac.stop();
                    PhatNhac.choinhac.release();
                    PhatNhac.choinhac = new MediaPlayer();
                    try{
                        if(PhatNhac.listBaihat != null) {
                            PhatNhac.choinhac.setDataSource(PhatNhac.listBaihat.get(PhatNhac.vitribai).getPath());
                            PhatNhac.choinhac.prepare();
                            PhatNhac.tenbh = PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle();
                            PhatNhac.tencs = PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle();
                            PhatNhac.tenbhcu = PhatNhac.tenbh;
                            tv_tenbaihat_index.setText(PhatNhac.tenbh);
                            tv_tencasi_index.setText(PhatNhac.tencs);
                            PhatNhac.choinhac.start();
                            iv_disk_index.startAnimation(diaxoay);
                            ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
                        }
                        else if(PhatNhac.listBaihatOnline != null){
                            new PlayMusicSync(MainActivity.this).execute();
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onResume() {
        if(PhatNhac.choinhac == null) {
            ll_thanhdieukhiennhac_index.setVisibility(View.INVISIBLE);
        }
        else{
            ll_thanhdieukhiennhac_index.setVisibility(View.VISIBLE);
        }
        if(PhatNhac.choinhac != null){
            if(PhatNhac.choinhac.isPlaying())
            {
                if(PhatNhac.listBaihat != null) {
                    tv_tenbaihat_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle());
                    tv_tencasi_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle());
                }else if(PhatNhac.listBaihatOnline != null){
                    tv_tenbaihat_index.setText(PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getTenBaiHat());
                    tv_tencasi_index.setText(PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getTenTacGia());
                }
                iv_disk_index.startAnimation(diaxoay);
                ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
            }
            else {
                if(PhatNhac.listBaihat != null) {
                    tv_tenbaihat_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle());
                    tv_tencasi_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle());
                }else if(PhatNhac.listBaihatOnline != null){
                    tv_tenbaihat_index.setText(PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getTenBaiHat());
                    tv_tencasi_index.setText(PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getTenTacGia());
                }
                ib_play_main.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                iv_disk_index.clearAnimation();
            }
        }
        super.onResume();
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public class PlayMusicSync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public PlayMusicSync(Context activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Đang tải bài hát...");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            PhatNhac.choinhac = new MediaPlayer();
            PhatNhac.choinhac.setAudioStreamType(AudioManager.STREAM_MUSIC);
            PhatNhac.choinhac.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.reset();
                }
            });
            try {
                PhatNhac.choinhac.setDataSource(PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getLink());
                PhatNhac.tenbh = PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getTenBaiHat();
                PhatNhac.tencs = PhatNhac.listBaihatOnline.get(PhatNhac.vitribai).getTenTacGia();
                PhatNhac.tenbhcu = PhatNhac.tenbh;
                PhatNhac.choinhac.prepare();
            } catch (IOException e) {

            }
            baitruocdo();
            baiketiep();
            return (null);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            PhatNhac.choinhac.start();
            tv_tenbaihat_index.setText(PhatNhac.tenbh);
            tv_tencasi_index.setText(PhatNhac.tencs);
            iv_disk_index.startAnimation(diaxoay);
        }
    }
}
