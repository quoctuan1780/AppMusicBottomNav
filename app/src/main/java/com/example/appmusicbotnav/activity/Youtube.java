package com.example.appmusicbotnav.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.YoutubeVideoAdapter;
import com.example.appmusicbotnav.modelYoutube.ModelHome;
import com.example.appmusicbotnav.service.YoutubeAPI;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;



public class Youtube extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;
    private static String id = "Gnu9EtD27os";
    private String json;
    private YoutubeVideoAdapter adapter;
    private RecyclerView rcv_video;
    private ImageView imageView;
    private ModelHome modelHome;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube);
        rcv_video = (RecyclerView) findViewById(R.id.rcv_video_tuong_tu);
        imageView = (ImageView) findViewById(R.id.ib_back_youtube);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ytp_phatvideo);
        if(getIntent().hasExtra("videoId")){
            id = getIntent().getStringExtra("videoId");
            json =  getIntent().getStringExtra("videoList");
        }
        modelHome = gson.fromJson(json, ModelHome.class);
        if(modelHome != null){
            adapter = new YoutubeVideoAdapter(Youtube.this, modelHome.getItems(), modelHome);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(RecyclerView.VERTICAL);
            rcv_video.setLayoutManager(manager);
            rcv_video.setAdapter(adapter);
        }
        khoiTaoPhatVideo(id);
        back();
    }

    private void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void khoiTaoPhatVideo(final String id) {
        youTubePlayerView.initialize(YoutubeAPI.KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(id);
                youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {

                    }

                    @Override
                    public void onPaused() {

                    }

                    @Override
                    public void onStopped() {

                    }

                    @Override
                    public void onBuffering(boolean b) {

                    }

                    @Override
                    public void onSeekTo(int i) {

                    }
                });
                youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
