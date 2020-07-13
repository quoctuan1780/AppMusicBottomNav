package com.example.appmusicbotnav.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import androidx.core.app.ActivityCompat;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.model.BaiHat;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhatNhac extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tv_tenbaihat, tv_tencasi, tv_tongtgbh, tv_tgchay;
    public ImageButton ib_lui, ib_toi, ib_play, ib_toi_10s, ib_lui_10s, ib_lap, ib_phatngaunhien, ib_download, ib_binhluan;
    private ImageView iv_disk;
    private SeekBar skThoigian;
    public static ArrayList<BaiHat> listBaihat;
    public static ArrayList<Baihat> listBaihatOnline;
    public static int vitribai, vitricu = -1, vitrinhac;
    public static MediaPlayer choinhac;
    private Animation animation;
    private static boolean lap = false, nghengaunhien = false;
    public static String tenbh = "", tencs = "", tenbhcu = "";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phatnhac);
        animation = AnimationUtils.loadAnimation(this, R.anim.disk_rotate);
        toolbar = (Toolbar) findViewById(R.id.tb_phatnhac);
        toolbar.setTitle("Phát nhạc");
        toolbar.setBackgroundColor(R.color.gray_color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        khoitao();
        if(kiemtranhacOnOff()){
            ib_download.setVisibility(View.INVISIBLE);
            ib_binhluan.setVisibility(View.INVISIBLE);
            phatnhacoffline();
        }
        else {
            ib_download.setVisibility(View.VISIBLE);
            ib_binhluan.setVisibility(View.VISIBLE);
            phatnhaconline();
            baitruocdo();
            baiketiep();
            dunghoacchoinhac();
            thaydoithanhchoinhac();
            phatlaphoackhong();
            nghengaunhienhoackhong();
            tangnhaclen10s();
            giamnhacxuong10s();
        }
    }

    private void khoitao(){
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
        ib_download = (ImageButton) findViewById(R.id.ib_download);
        ib_binhluan = (ImageButton) findViewById(R.id.ib_binhluan);
    }

    private boolean kiemtranhacOnOff(){
        if(getIntent().hasExtra("list")) {
            vitribai = getIntent().getIntExtra("vitri", 0);
            listBaihat = (ArrayList<BaiHat>) getIntent().getSerializableExtra("list");
            return true;
        }
        else if (getIntent().hasExtra("listonline")) {
            vitribai = getIntent().getIntExtra("vitri", 0);
            listBaihatOnline = getIntent().getParcelableArrayListExtra("listonline");
        }
        return false;
    }

    private void phatnhacoffline(){
        try{
            listBaihatOnline = null;
        }catch(Exception e){

        }
        tenbh = listBaihat.get(vitribai).getTitle();
        tencs = listBaihat.get(vitribai).getSubTitle();
        try {
            khoitaonhacoff();
        } catch (IOException e) {

        }
        dunghoacchoinhac();
        phatlaphoackhong();
        nghengaunhienhoackhong();
        baiketiep();
        baitruocdo();
        tangnhaclen10s();
        giamnhacxuong10s();
        chuyenbaitudongOff();
    }

    private void phatnhaconline(){
        try{
            listBaihat = null;
        }catch(Exception e){

        }
        tenbh = listBaihatOnline.get(vitribai).getTenBaiHat();
        tencs = listBaihatOnline.get(vitribai).getTenTacGia();
        try {
            khoitaonhacnon();
        } catch (IOException e) {
        }
    }

    private void khoitaonhacnon() throws IOException {
        if(choinhac != null) {
            if(choinhac.isPlaying()) {
                if (tenbhcu.equals(tenbh)) {
                    vitrinhac = choinhac.getCurrentPosition();
                    capnhatthoigiannhac();
                    tv_tenbaihat.setText(tenbh);
                    tv_tencasi.setText(tencs);
                    ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
                    iv_disk.startAnimation(animation);
                    tongthoigiannhac();
                } else {
                    choinhac.stop();
                    choinhac.release();
                    khoitaonhacon(vitribai);
                }
            }else {
                choinhac.stop();
                choinhac.release();
                khoitaonhacon(vitribai);
            }
        }
        else {
            khoitaonhacon(vitribai);
        }
    }

    private void khoitaonhacon(int vitribai) throws IOException {
        new PlayMusicSync(PhatNhac.this).execute();
    }

    private void khoitaonhacoff() throws IOException {
        if(choinhac != null) {
            if(choinhac.isPlaying()) {
                if (tenbhcu.equals(tenbh)) {
                    vitrinhac = choinhac.getCurrentPosition();
                    capnhatthoigiannhac();
                    tv_tenbaihat.setText(tenbh);
                    tv_tencasi.setText(tencs);
                    ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
                    iv_disk.startAnimation(animation);
                    tongthoigiannhac();
                } else {
                    choinhac.stop();
                    choinhac.release();
                    khoitaonhacoff(vitribai);
                }
            }else {
                choinhac.stop();
                choinhac.release();
                khoitaonhacoff(vitribai);
            }
        }
        else {
            khoitaonhacoff(vitribai);
        }
    }

    private void khoitaonhacoff(int vitribai) throws IOException {
        choinhac = new MediaPlayer();
        choinhac.setDataSource(listBaihat.get(vitribai).getPath());
        choinhac.prepare();
        tenbh = listBaihat.get(vitribai).getTitle();
        tencs = listBaihat.get(vitribai).getSubTitle();
        tv_tenbaihat.setText(tenbh);
        tv_tencasi.setText(tencs);
        choinhac.start();
        ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
        iv_disk.startAnimation(animation);
        capnhatthoigiannhac();
        tongthoigiannhac();
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
                if(listBaihatOnline != null){
                    chuyenbaitudongOn();
                }
//                choinhac.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        vitribai++;
//                        if(listBaihat != null) {
//                            if (vitribai > listBaihat.size() - 1) {
//                                vitribai = 0;
//                            }
//                        }else if(listBaihatOnline != null){
//                            if (vitribai > listBaihatOnline.size() - 1) {
//                                vitribai = 0;
//                            }
//                        }
//                        if (choinhac.isPlaying()) {
//                            choinhac.stop();
//                            choinhac.release();
//                            ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
//                        }
//
//                        if (lap)
//                            choinhac.setLooping(true);
//                        else{
//                            if(nghengaunhien) {
//                                try {
//                                    nghengaunhien();
//                                } catch (IOException e) {
//
//                                }
//                            }
//                            else {
//                                if(listBaihat != null) {
//                                    try {
//                                        khoitaonhacoff(vitribai);
//                                    } catch (IOException e) {
//
//                                    }
//                                }else{
//                                    try {
//                                        khoitaonhacon(vitribai);
//                                    } catch (IOException e) {
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                });
            }
        }, 100);
    }

    private void chuyenbaitudongOff(){
        choinhac.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vitribai++;
                if (vitribai > listBaihat.size() - 1) {
                    vitribai = 0;
                }
                if (choinhac.isPlaying()) {
                    choinhac.stop();
                    choinhac.release();
                    ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }

                if (lap)
                    choinhac.setLooping(true);
                else {
                    try {
                        if (nghengaunhien)
                            nghengaunhien();
                        else
                            khoitaonhacoff(vitribai);
                    }
                    catch(Exception e){

                    }
                }
            }
        });
    }

    private void chuyenbaitudongOn(){
        choinhac.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vitribai++;
                if (vitribai > listBaihatOnline.size() - 1) {
                    vitribai = 0;
                }
                if (choinhac.isPlaying()) {
                    choinhac.stop();
                    choinhac.release();
                    ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }

                if (lap)
                    choinhac.setLooping(true);
                else {
                    try {
                        if (nghengaunhien)
                            nghengaunhien();
                        else
                            new PlayMusicSync(PhatNhac.this).execute();
                    }
                    catch(Exception e){

                    }
                }
            }
        });
    }

    private void tongthoigiannhac(){
        SimpleDateFormat dinhdangtg = new SimpleDateFormat("mm:ss");
        tv_tongtgbh.setText(dinhdangtg.format(choinhac.getDuration()));
        skThoigian.setMax(choinhac.getDuration());
        thaydoithanhchoinhac();
    }

    private void phatlaphoackhong(){
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

    private void nghengaunhienhoackhong(){
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

    private void dunghoacchoinhac(){
        ib_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choinhac != null){
                    if(choinhac.isPlaying()){
                        choinhac.pause();
                        ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        iv_disk.clearAnimation();
                    }else{
                        choinhac.start();
                        ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
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
                try {
                    vitribai++;
                    if (listBaihat != null) {
                        if (vitribai > listBaihat.size() - 1) {
                            vitribai = 0;
                        }
                    } else if (listBaihatOnline != null) {
                        if (vitribai > listBaihatOnline.size() - 1) {
                            vitribai = 0;
                        }
                    }
                    if (choinhac.isPlaying()) {
                        choinhac.stop();
                        ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    if (lap)
                        choinhac.setLooping(true);

                    if (nghengaunhien) {
                        nghengaunhien();
                    } else {
                        if (listBaihatOnline == null) {
                            khoitaonhacoff(vitribai);
                        } else {
                            khoitaonhacon(vitribai);
                        }
                    }
                }catch(Exception e){

                }
            }
        });
    }

    private void baitruocdo(){
        ib_lui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    vitribai--;
                    if (listBaihat != null) {
                        if (vitribai < 0) {
                            vitribai = listBaihat.size() - 1;
                        }
                    } else if (listBaihatOnline != null) {
                        if (vitribai < 0) {
                            vitribai = listBaihatOnline.size() - 1;
                        }
                    }
                    if (choinhac.isPlaying()) {
                        choinhac.stop();
                        ib_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    if (lap)
                        choinhac.setLooping(true);

                    if (nghengaunhien) {
                        nghengaunhien();
                    } else {
                        if (listBaihat != null) {
                            khoitaonhacoff(vitribai);
                        } else {
                            khoitaonhacon(vitribai);
                        }
                    }
                } catch (Exception e){

                }
            }
        });
    }

    private void tangnhaclen10s(){
        ib_toi_10s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int thoigianhientai = choinhac.getCurrentPosition();
                    thoigianhientai += 10000;
                    if (thoigianhientai > choinhac.getDuration()) {
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
                        if (nghengaunhien) {
                            nghengaunhien();
                        } else {
                            if (listBaihat != null) {
                                khoitaonhacoff(vitribai);
                            } else {
                                khoitaonhacon(vitribai);
                            }
                        }
                    } else
                        choinhac.seekTo(thoigianhientai);
                }catch(Exception e){

                }
            }
        });
    }

    private void giamnhacxuong10s(){
        ib_lui_10s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int thoigianhientai = choinhac.getCurrentPosition();
                    thoigianhientai -= 10000;
                    if (thoigianhientai < 0) {
                        choinhac.stop();
                        if (lap)
                            choinhac.setLooping(true);
                        if (listBaihat != null) {
                            khoitaonhacoff(vitribai);
                        } else {
                            khoitaonhacon(vitribai);
                        }
                    } else
                        choinhac.seekTo(thoigianhientai);
                }catch(Exception e){

                }
            }
        });
    }

    private void thaydoithanhchoinhac(){
        skThoigian.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
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

    private void nghengaunhien() throws IOException {
        Random rd = new Random();
        int vitri;
        if (listBaihat != null) {
            vitri = rd.nextInt(listBaihat.size() - 0) + 0;
            vitribai = vitri;
            khoitaonhacoff(vitri);
        } else {
            vitri = rd.nextInt(listBaihatOnline.size() - 0) + 0;
            vitribai = vitri;
            new PlayMusicSync(PhatNhac.this).execute();
        }
    }

    private void tangluotnghe(int id){
        try {
            DataService dataService = APIService.getService();
            Call<Void> tangluotnghe = dataService.tangluotthich(id);
            tangluotnghe.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(nghengaunhien){
            ib_phatngaunhien.setImageResource(R.drawable.ic_random_is_random_24dp);
        }
        else {
            ib_phatngaunhien.setImageResource(R.drawable.ic_random_is_not_random_24dp);
        }
        if(lap){
            ib_lap.setImageResource(R.drawable.ic_loop_is_loop_24dp);
        }
        else{
            ib_lap.setImageResource(R.drawable.ic_loop_is_not_loop_24dp);
        }
    }

    private void taiNhac(){
        if(ib_download.getVisibility() == View.VISIBLE) {
            ib_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(PhatNhac.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            });
        }
    }

    private void binhluan(){
        ib_binhluan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhatNhac.this, BinhLuan.class);
                intent.putExtra("idBaihat", listBaihatOnline.get(vitribai).getIdBaiHat());
                intent.putExtra("tenBaihat", listBaihatOnline.get(vitribai).getTenBaiHat());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String link = listBaihatOnline.get(vitribai).getLink();
                    DownloadManager downloadManager;
                    File file = Environment.getExternalStorageDirectory();
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(link);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, listBaihatOnline.get(vitribai).getTenBaiHat()+".mp3");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long downloadID = downloadManager.enqueue(request);
                    Toast.makeText(PhatNhac.this, "Đang tải bài hát...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PhatNhac.this, "Bạn chưa gán quyền truy cập để ghi vào bộ nhớ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        tenbhcu = tenbh;
        vitricu = vitribai;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(listBaihatOnline != null && choinhac != null){
            chuyenbaitudongOn();
            taiNhac();
            binhluan();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            tenbhcu = tenbh;
            vitricu = vitribai;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class PlayMusicSync extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public PlayMusicSync(Context activity) {
            if(activity != null)
                dialog = new ProgressDialog(activity);
            else dialog = null;
        }

        @Override
        protected void onPreExecute() {
            if(dialog != null) {
                dialog.setMessage("Đang tải bài hát...");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        }
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            choinhac = new MediaPlayer();
            choinhac.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                choinhac.setDataSource(listBaihatOnline.get(vitribai).getLink());
                tangluotnghe(listBaihatOnline.get(vitribai).getIdBaiHat());
                choinhac.prepare();
            } catch (IOException e) {

            }
            baitruocdo();
            baiketiep();
            dunghoacchoinhac();
            thaydoithanhchoinhac();
            phatlaphoackhong();
            nghengaunhienhoackhong();
            tangnhaclen10s();
            giamnhacxuong10s();
            taiNhac();
            binhluan();
            return (null);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(PhatNhac.this != null) {
                dialog.dismiss();
            }
            ib_play.setImageResource(R.drawable.ic_pause_black_24dp);
            choinhac.start();
            tenbh = listBaihatOnline.get(vitribai).getTenBaiHat();
            tencs = listBaihatOnline.get(vitribai).getTenTacGia();
            tv_tenbaihat.setText(tenbh);
            tv_tencasi.setText(tencs);
            iv_disk.startAnimation(animation);
            chuyenbaitudongOn();
            capnhatthoigiannhac();
            tongthoigiannhac();
        }
    }
}
