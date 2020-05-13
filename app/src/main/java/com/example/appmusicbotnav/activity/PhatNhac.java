package com.example.appmusicbotnav.activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.model.BaiHat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;


public class PhatNhac extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener {
    private Toolbar toolbar;
    private TextView tv_tenbaihat, tv_tencasi, tv_tongtgbh, tv_tgchay;
    private ImageButton ib_lui, ib_toi, ib_play, ib_toi_10s, ib_lui_10s, ib_lap, ib_phatngaunhien;
    ImageView iv_disk;
    private SeekBar skThoigian;
    public static ArrayList<BaiHat> listBaihat;
    public static int vitribai, vitricu = -1, vitrinhac;
    public static MediaPlayer choinhac;
    private Animation animation;
    private static MediaPlayer choinhaccu = null;
    private static boolean lap = false, nghengaunhien = false;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phatnhac);
        Log.i("TAG", "onCreate: ");
        //Lấy dữ liệu từ fragment danh sách bài hát offline qua Activity phát nhạc
        vitribai = getIntent().getIntExtra("vitri", 0);
        listBaihat = (ArrayList<BaiHat>) getIntent().getSerializableExtra("list");
        //Khởi tạo đĩa xoay
        animation = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);
        toolbar = (Toolbar) findViewById(R.id.tb_phatnhac);
        toolbar.setTitle("Phát nhạc");
        toolbar.setBackgroundColor(R.color.gray_color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anhxa();
        //Vừa mở bài hát phát nhạc luôn
        if(listBaihat != null){
            khoitaonhaccodieukien();
            //Hát sau khi tương tác với các nút
            baitruocdo();
            play();
            baiketiep();
            thaydoithanhchoinhac();
            nhannutlaplai();
            nhannutnghengaunhien();
            ib_toi_10s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tangnhaclen10s();
                }
            });
            ib_lui_10s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giamnhacxuong10s();
                }
            });
            choinhac.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    vitribai++;
                    if(vitribai > listBaihat.size() - 1){
                        vitribai = 0;
                    }
                    if(choinhac.isPlaying()){
                        choinhac.stop();
                        ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    if(lap)
                        choinhac.setLooping(true);

                    if(nghengaunhien)
                        nghengaunhien();
                    else
                        phatnhac();
                }
            });
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        skThoigian.setSecondaryProgress(percent);
    }

    @Override
    protected void onStart() {
        Log.i("TAG", "Start");
        if(choinhaccu != null) {
            choinhaccu.stop();
        }
        super.onStart();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            vitricu = vitribai;
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void anhxa(){
        tv_tenbaihat = (TextView) findViewById(R.id.tv_tenbaihat);
        tv_tencasi = (TextView) findViewById(R.id.tv_tencasi);
        tv_tgchay = (TextView) findViewById(R.id.tv_thoigianchay);
        tv_tongtgbh = (TextView) findViewById(R.id.tv_tongthoigian);
        ib_lui = (ImageButton) findViewById(R.id.ib_baitruoc);
        ib_toi = (ImageButton) findViewById(R.id.ib_baiketiep);
        ib_play = (ImageButton) findViewById(R.id.ib_choinhac);
        ib_toi_10s = (ImageButton) findViewById(R.id.ib_toi_10_s);
        ib_lui_10s = (ImageButton) findViewById(R.id.ib_lui_10_s);
        ib_lap = (ImageButton) findViewById(R.id.ib_laplai);
        ib_phatngaunhien = (ImageButton) findViewById(R.id.ib_nghengaunhien);
        skThoigian = (SeekBar) findViewById(R.id.sk_phatnhac);
        iv_disk = (ImageView) findViewById(R.id.iv_dia_phat_nhac);
    }

    private void khoitaonhaccodieukien(){
        if(choinhac != null) {
            if(choinhac.isPlaying()) {
                if (vitricu == vitribai) {
                    vitrinhac = choinhac.getCurrentPosition();
                    khoitaohatchidinh(vitribai);
                    choinhac.seekTo(vitrinhac);
                } else {
                    choinhac.stop();
                    khoitaohatchidinh(vitribai);
                }
            }else {
                choinhac.stop();
                khoitaohatchidinh(vitribai);
            }
        }
        else {
            khoitaohatchidinh(vitribai);
        }
    }

    private void nhannutnghengaunhien(){
        ib_phatngaunhien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nghengaunhien){
                    nghengaunhien = false;
                    ib_phatngaunhien.setImageResource(R.drawable.ic_random_is_not_random_24dp);
                }
                else {
                    nghengaunhien = true;
                    ib_phatngaunhien.setImageResource(R.drawable.ic_random_is_random_24dp);
                }
            }
        });
    }
    private void nghengaunhien(){
        Random rd = new Random();
        int vitri = rd.nextInt(listBaihat.size() - 0) + 0;
        khoitaohatchidinh(vitri);
        choinhac.start();
        ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
        tongthoigiannhac();
        capnhatthoigiannhac();
        iv_disk.startAnimation(animation);
    }

    private void nhannutlaplai(){
        ib_lap.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(lap == false){
                    lap = true;
                    choinhac.setLooping(true);
                    ib_lap.setImageResource(R.drawable.ic_loop_is_loop_24dp);
                }
                else{
                    lap = false;
                    choinhac.setLooping(false);
                    ib_lap.setImageResource(R.drawable.ic_loop_is_not_loop_24dp);
                }
            }
        });
    }

    private void phatnhac(){
        khoitaonhac();
        choinhac.start();
        ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
        tongthoigiannhac();
        capnhatthoigiannhac();
        iv_disk.startAnimation(animation);
    }

    private void khoitaohatchidinh(int vitribai){
        if(choinhac != null && choinhac.isPlaying()) choinhac.stop();
//        choinhac = MediaPlayer.create(PhatNhac.this, listBaihat.get(vitribai).getFile());
        choinhac = new MediaPlayer();
        try {
            choinhac.setDataSource(listBaihat.get(vitribai).getPath());
            choinhac.prepare();
            tv_tenbaihat.setText(listBaihat.get(vitribai).getTitle());
            tv_tencasi.setText(listBaihat.get(vitribai).getSubTitle());
            ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
            choinhac.start();
            iv_disk.startAnimation(animation);
            capnhatthoigiannhac();
            tongthoigiannhac();
            iv_disk.startAnimation(animation);
        }catch(Exception e){
            Toast.makeText(PhatNhac.this, "Khong the phat duoc nhac", Toast.LENGTH_LONG).show();
        }
    }

    private void khoitaonhac(){
        choinhac = new MediaPlayer();
        try {
            choinhac.setDataSource(listBaihat.get(vitribai).getPath());
            choinhac.prepare();
            tv_tenbaihat.setText(listBaihat.get(vitribai).getTitle());
            tv_tencasi.setText(listBaihat.get(vitribai).getSubTitle());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void play(){
        ib_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choinhac != null) {
                    if (choinhac.isPlaying()) {
                        choinhac.pause();
                        ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        iv_disk.clearAnimation();
                    } else {
                        choinhac.start();
                        ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
                        tongthoigiannhac();
                        capnhatthoigiannhac();
                        iv_disk.startAnimation(animation);
                    }
                }
            }
        });
    }

    private void baiketiep(){
        ib_toi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vitribai++;
                if(vitribai > listBaihat.size() - 1){
                    vitribai = 0;
                }
                if(choinhac.isPlaying()){
                    choinhac.stop();
                    ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                if(lap)
                    choinhac.setLooping(true);

                    if(nghengaunhien)
                        nghengaunhien();
                    else
                        phatnhac();

            }
        });
    }

    private void baitruocdo(){
        ib_lui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vitribai--;
                if(vitribai < 0){
                    vitribai = listBaihat.size() - 1;
                }
                if(choinhac.isPlaying()){
                    choinhac.stop();
                    ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                if(lap)
                    choinhac.setLooping(true);

                    if(nghengaunhien)
                        nghengaunhien();
                    else
                        phatnhac();

            }
        });
    }

    private void tangnhaclen10s(){
        int thoigianhientai = choinhac.getCurrentPosition();
        thoigianhientai += 10000;
        if(thoigianhientai > choinhac.getDuration()){
            vitribai++;
            if(vitribai > listBaihat.size() - 1){
                vitribai = 0;
            }
            if(choinhac.isPlaying()){
                choinhac.stop();
                ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
            if(lap)
                choinhac.setLooping(true);
            if (nghengaunhien)
                nghengaunhien();
            else
                phatnhac();
        }
        else
        choinhac.seekTo(thoigianhientai);
    }

    private void giamnhacxuong10s(){
        int thoigianhientai = choinhac.getCurrentPosition();
        thoigianhientai -= 10000;
        if(thoigianhientai < 0){
            choinhac.stop();
            if(lap)
                choinhac.setLooping(true);
            phatnhac();
        }
        else
            choinhac.seekTo(thoigianhientai);
    }

    private void thaydoithanhchoinhac(){
        skThoigian.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                choinhac.seekTo(skThoigian.getProgress());
                if(lap) {
                    choinhac.setLooping(true);
                }
            }
        });
    }

    private void capnhatthoigiannhac(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat capnhatgio = new SimpleDateFormat("mm:ss");
                tv_tgchay.setText(capnhatgio.format(choinhac.getCurrentPosition()));
                handler.postDelayed(this, 500);
                //cập nhật thanh seekbar chạy theo nhạc
                skThoigian.setProgress(choinhac.getCurrentPosition());

                //Kiểm tra thời gian kết thúc bài hát chuyển next
                choinhac.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        vitribai++;
                        if (vitribai > listBaihat.size() - 1) {
                            vitribai = 0;
                        }
                        if (choinhac.isPlaying()) {
                            choinhac.stop();
                            ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        }
                        if (lap)
                            choinhac.setLooping(true);
                        else{
                            if(nghengaunhien)
                                nghengaunhien();
                            else
                                phatnhac();
                        }
                    }
                });
            }
        }, 100);
    }

    private void tongthoigiannhac(){
        SimpleDateFormat dinhdangtg = new SimpleDateFormat("mm:ss");
        tv_tongtgbh.setText(dinhdangtg.format(choinhac.getDuration()));

        skThoigian.setMax(choinhac.getDuration());
        thaydoithanhchoinhac();
    }
}
