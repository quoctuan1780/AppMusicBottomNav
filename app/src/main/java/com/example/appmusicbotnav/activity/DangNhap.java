package com.example.appmusicbotnav.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.modelOnline.Authenticate;
import com.example.appmusicbotnav.modelOnline.Thongtintaikhoan;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.modelOnline.Taikhoan;
import com.example.appmusicbotnav.session.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhap extends AppCompatActivity {
    private EditText et_tendangnhap, et_matkhau;
    private Button bt_dangnhap;
    private ProgressDialog dialog;
    private ConstraintLayout cl_dangnhap;
    private TextView tv_dangki;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        khoiTao();
        kiemTraDangNhap();
        dangKi();
        cl_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(DangNhap.this, v);
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void khoiTao(){
        et_tendangnhap = (EditText) findViewById(R.id.et_tendangnhap);
        et_matkhau = (EditText) findViewById(R.id.et_matkhau);
        bt_dangnhap = (Button) findViewById(R.id.bt_dangnhap);
        cl_dangnhap = (ConstraintLayout) findViewById(R.id.cl_dangnhap);
        tv_dangki = (TextView) findViewById(R.id.tv_dangkingay);
    }

    private void kiemTraDangNhap(){
        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_tendangnhap.getText().toString().equals("") || et_matkhau.getText().toString().equals("")){
                    Toast.makeText(DangNhap.this, "Tên đăng nhập hoặc mật khẩu không được để trống", Toast.LENGTH_LONG).show();
                }
                else{
                    DataService dataService = APIService.getService();
                    final Session session = new Session(DangNhap.this);

                    dialog = new ProgressDialog(DangNhap.this);
                    dialog.setMessage("Đang đăng nhập, xin hãy đợi 30s...");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    Call<Thongtintaikhoan> thongtintaikhoanCall = dataService.Laythongtintaikhoan(et_tendangnhap.getText().toString());
                    if(isNetworkConnected()) {
                        thongtintaikhoanCall.enqueue(new Callback<Thongtintaikhoan>() {
                            @Override
                            public void onResponse(Call<Thongtintaikhoan> call, Response<Thongtintaikhoan> response) {
                                if (response.isSuccessful()) {
                                    Thongtintaikhoan thongtintaikhoan = response.body();
                                    session.setTen(thongtintaikhoan.getTen());
                                    session.setEmail(thongtintaikhoan.getEmail());
                                    session.setId(thongtintaikhoan.getIdNguoiDung());
                                    Taikhoan login = new Taikhoan();
                                    login.setTen(et_tendangnhap.getText().toString());
                                    login.setPass(et_matkhau.getText().toString());
                                    DataService dataService1 = APIService.getService();
                                    Call<Authenticate> authenticateCall = dataService1.Dangnhap(login);
                                    authenticateCall.enqueue(new Callback<Authenticate>() {
                                        @Override
                                        public void onResponse(Call<Authenticate> call, Response<Authenticate> response) {
                                            if (response.isSuccessful()) {
                                                Authenticate authenticate = response.body();
                                                dialog.dismiss();
                                                session.setToken(authenticate.getToken());
                                                Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        onBackPressed();
                                                    }
                                                }, 1000);
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(DangNhap.this, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Authenticate> call, Throwable t) {
                                            call.cancel();
                                            dialog.dismiss();
                                            Toast.makeText(DangNhap.this, "Đăng nhập thất bại, kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(DangNhap.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Thongtintaikhoan> call, Throwable t) {
                                call.cancel();
                                dialog.dismiss();
                                Toast.makeText(DangNhap.this, "Đăng nhập thất bại, kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        dialog.dismiss();
                        Toast.makeText(DangNhap.this, "Kiểm tra lại kết nối Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void dangKi(){
        tv_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, DangKi.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
