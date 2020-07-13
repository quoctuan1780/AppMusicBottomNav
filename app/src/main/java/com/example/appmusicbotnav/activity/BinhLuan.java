package com.example.appmusicbotnav.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.BinhLuanAdapter;
import com.example.appmusicbotnav.modelOnline.Comment;
import com.example.appmusicbotnav.modelOnline.Taikhoan;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.session.Session;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BinhLuan extends AppCompatActivity {
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private TextView tv_tenbaihat;
    private int idBaihat;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private ArrayList<Taikhoan> taikhoanArrayList = new ArrayList<>();
    private BinhLuanAdapter adapter;
    private RecyclerView rcv_binhluan;
    private String tenBaihat;
    private Session session;
    private EditText et_noidung;
    private ImageButton ib_binhluan;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binhluan);
        constraintLayout = (ConstraintLayout) findViewById(R.id.cl_binhluan);
        rcv_binhluan = (RecyclerView) findViewById(R.id.rcv_hien_thi_binh_luan);
        tv_tenbaihat = (TextView) findViewById(R.id.tv_tenbaihat_binhluan);
        et_noidung = (EditText) findViewById(R.id.et_binhluan);
        ib_binhluan = (ImageButton) findViewById(R.id.ib_binhluan);
        toolbar = (Toolbar) findViewById(R.id.tb_binhluan);
        toolbar.setBackgroundColor(R.color.gray_color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new Session(BinhLuan.this);
        if(getIntent().hasExtra("idBaihat")){
            idBaihat = getIntent().getIntExtra("idBaihat", -1);
            tenBaihat = getIntent().getStringExtra("tenBaihat");
        }
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(BinhLuan.this, v);
                return false;
            }
        });

        layBinhLuan();
        binhLuan();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void layBinhLuan(){
        if(idBaihat == -1){
            Toast.makeText(this, "Không lấy được bình luận", Toast.LENGTH_SHORT).show();
        }else{
            tv_tenbaihat.setText("Tên bài hát: " + tenBaihat);
            DataService dataService = APIService.getService();
            Call<List<Comment>> listCall = dataService.layComment(idBaihat);
            listCall.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if(response.isSuccessful() && response.code() == 200){
                        commentArrayList = (ArrayList<Comment>) response.body();
                        adapter = new BinhLuanAdapter(BinhLuan.this, commentArrayList);
                        LinearLayoutManager manager = new LinearLayoutManager(BinhLuan.this);
                        manager.setOrientation(RecyclerView.VERTICAL);
                        rcv_binhluan.setLayoutManager(manager);
                        rcv_binhluan.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {

                }
            });
        }
    }

    private void binhLuan(){
        ib_binhluan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Comment comment = new Comment();
                comment.setIdBaiHat(idBaihat);
                comment.setIdNguoiDung(session.getId());
                comment.setNoiDung(et_noidung.getText().toString());
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String date = df.format("yyyy-MM-dd", Calendar.getInstance().getTime()).toString();
                comment.setThoiDiemBinhLuan(date);
                String token = session.getToken();
                String content = "Bearer " + token;
                DataService dataService = APIService.getServicePlaylist();
                Call<Comment> commentCall = dataService.comment(content, comment);
                commentCall.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if(response.code() == 401){
                            Toast.makeText(BinhLuan.this, "Bạn đã hết phiên đăng nhập, hãy đăng nhập lại để bình luận", Toast.LENGTH_SHORT).show();
                        }else
                        if(response.isSuccessful()){
                            adapter.updateAdapter(response.body());
                            et_noidung.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
