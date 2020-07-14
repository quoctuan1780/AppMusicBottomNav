package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.YoutubeVideoAdapter;
import com.example.appmusicbotnav.modelYoutube.ModelHome;
import com.example.appmusicbotnav.modelYoutube.VideoYT;
import com.example.appmusicbotnav.service.YoutubeAPI;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Karaoke extends Fragment {
    private View view;
    private Toolbar toolbar;
    private EditText et_timkiemvideo;
    private Button bt_timkiemvideo;
    private RecyclerView rcv_hienthivideo;
    private static ModelHome modelHome;
    private RelativeLayout relativeLayout;
    private List<VideoYT> videoList;
    private YoutubeVideoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_karaoke, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.tb_karaoke);
        et_timkiemvideo = (EditText) view.findViewById(R.id.et_timkiem_video_karaoke);
        bt_timkiemvideo = (Button) view.findViewById(R.id.bt_timkiem_karaoke);
        rcv_hienthivideo = (RecyclerView) view.findViewById(R.id.rcv_hien_thi_video_karaoke);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_karaoke);
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
        timKiemVideo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(modelHome != null) {
            videoList = modelHome.getItems();
            adapter = new YoutubeVideoAdapter(getContext(), videoList, modelHome);
            LinearLayoutManager manager;
            manager = new LinearLayoutManager(getContext());
            manager.setOrientation(RecyclerView.VERTICAL);
            rcv_hienthivideo.setLayoutManager(manager);
            rcv_hienthivideo.setAdapter(adapter);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void timKiemVideo(){
        bt_timkiemvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(getContext(), v);
                if(!et_timkiemvideo.getText().toString().equals("")){
                    layDuLieuVideo(et_timkiemvideo.getText().toString());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(modelHome != null) {
                                videoList = modelHome.getItems();
                                adapter = new YoutubeVideoAdapter(getContext(), videoList, modelHome);
                                LinearLayoutManager manager;
                                manager = new LinearLayoutManager(getContext());
                                manager.setOrientation(RecyclerView.VERTICAL);
                                rcv_hienthivideo.setLayoutManager(manager);
                                rcv_hienthivideo.setAdapter(adapter);
                            }
                        }
                    }, 1000);
                }
            }
        });
    }

    private void layDuLieuVideo(String query) {
        String url= YoutubeAPI.BASE_URL + YoutubeAPI.sch + YoutubeAPI.KEY + YoutubeAPI.mx  + YoutubeAPI.part +YoutubeAPI.query + query + YoutubeAPI.type;
        Call<ModelHome> data = YoutubeAPI.getHomeVideo().getYT(url);
        data.enqueue(new Callback<ModelHome>() {
            @Override
            public void onResponse(Call<ModelHome> call, Response<ModelHome> response) {
                if(response.code() == 403){
                    Toast.makeText(getContext(), "Bạn đã dùng hết lượt hát ngày hôm nay, ngày mai hãy quay lại", Toast.LENGTH_SHORT).show();
                }else if(response.isSuccessful()){
                    modelHome = response.body();
                }

            }

            @Override
            public void onFailure(Call<ModelHome> call, Throwable t) {
                Toast.makeText(getContext(), "Kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

