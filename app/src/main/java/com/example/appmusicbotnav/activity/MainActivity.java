package com.example.appmusicbotnav.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appmusicbotnav.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {
    private Animation diaxoay;
    private BottomNavigationView nav_thuvien;
    private NavController nav_con_thuvien;
    private ImageView ib_play_main, ib_baiketiep_index, ib_baitruoc_index, iv_disk_index;
    private TextView tv_tenbaihat_index, tv_tencasi_index;
    private LinearLayout ll_thanhdieukhiennhac_index1;
    private int vitribai = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav_thuvien = (BottomNavigationView) findViewById(R.id.nav_thuvien);
        nav_con_thuvien = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(nav_thuvien, nav_con_thuvien);
        diaxoay = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);
        anhxa();
        baitruocdo();
        playnhac();
        baiketiep();

        ll_thanhdieukhiennhac_index1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhatNhac.class);
                if(vitribai == -1){
                    startActivity(intent);
                }
                else{
                    intent.putExtra("vitri", PhatNhac.vitribai);
                    intent.putExtra("list", (Serializable) PhatNhac.listBaihat);
                    startActivity(intent);
                }
            }
        });
    }

    private void anhxa(){
        ib_play_main = (ImageView) findViewById(R.id.ib_choinhac_index);
        ib_baiketiep_index = (ImageView) findViewById(R.id.ib_baiketiep_index);
        ib_baitruoc_index = (ImageView) findViewById(R.id.ib_baitruoc_index);
        iv_disk_index = (ImageView) findViewById(R.id.iv_disk_index);
        tv_tenbaihat_index = (TextView) findViewById(R.id.tv_tenbaihat_index);
        tv_tencasi_index = (TextView) findViewById(R.id.tv_tencasi_index);
        ll_thanhdieukhiennhac_index1 = (LinearLayout) findViewById(R.id.ll_thanhdieukhiennhac_index1);
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
                    if(PhatNhac.vitribai > PhatNhac.listBaihat.size() - 1){
                        PhatNhac.vitribai = 0;
                    }
                    PhatNhac.choinhac.stop();
                    PhatNhac.choinhac = MediaPlayer.create(getBaseContext(), PhatNhac.listBaihat.get(PhatNhac.vitribai).getFile());
                    tv_tenbaihat_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle());
                    tv_tencasi_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle());
                    PhatNhac.choinhac.start();
                    iv_disk_index.startAnimation(diaxoay);
                    ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
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
                    if (PhatNhac.vitribai < 0) {
                        PhatNhac.vitribai = PhatNhac.listBaihat.size() - 1;
                    }
                    PhatNhac.choinhac.stop();
                    PhatNhac.choinhac = MediaPlayer.create(getBaseContext(), PhatNhac.listBaihat.get(PhatNhac.vitribai).getFile());
                    tv_tenbaihat_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle());
                    tv_tencasi_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle());
                    PhatNhac.choinhac.start();
                    iv_disk_index.startAnimation(diaxoay);
                    ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        Log.i("checkMain", "start");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i("checkMain", "stop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i("checkMain", "Resume");
        if(PhatNhac.choinhac != null){
            if(PhatNhac.choinhac.isPlaying())
            {
                vitribai = PhatNhac.vitribai;
                tv_tenbaihat_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getTitle());
                tv_tencasi_index.setText(PhatNhac.listBaihat.get(PhatNhac.vitribai).getSubTitle());
                iv_disk_index.startAnimation(diaxoay);
                ib_play_main.setImageResource(R.drawable.ic_pause_black_24dp);
            }
            else {
                ib_play_main.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                iv_disk_index.clearAnimation();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("checkMain", "pause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i("checkMain", "restart");
        super.onRestart();
    }
}
