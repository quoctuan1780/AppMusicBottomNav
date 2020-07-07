package com.example.appmusicbotnav.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        khoiTao();
        kiemTraDangNhap();
    }

    private void khoiTao(){
        et_tendangnhap = (EditText) findViewById(R.id.et_tendangnhap);
        et_matkhau = (EditText) findViewById(R.id.et_matkhau);
        bt_dangnhap = (Button) findViewById(R.id.bt_dangnhap);
    }

    private void kiemTraDangNhap(){
        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_tendangnhap.getText().equals("") || et_matkhau.getText().equals("")){
                    Toast.makeText(DangNhap.this, "Tên đăng nhập hoặc mật khẩu không được để trống", Toast.LENGTH_LONG).show();
                }
                else{
                    DataService dataService = APIService.getService();
                    final Session session = new Session(DangNhap.this);
                    Taikhoan login = new Taikhoan();
                    login.setTen(et_tendangnhap.getText().toString());
                    login.setPass(et_matkhau.getText().toString());
                    Call<Authenticate> call = dataService.Dangnhap(login);
                    dialog = new ProgressDialog(DangNhap.this);
                    dialog.setMessage("Đang đăng nhập...");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    Call<Thongtintaikhoan> thongtintaikhoanCall = dataService.Laythongtintaikhoan(et_tendangnhap.getText().toString());
                    thongtintaikhoanCall.enqueue(new Callback<Thongtintaikhoan>() {
                        @Override
                        public void onResponse(Call<Thongtintaikhoan> call, Response<Thongtintaikhoan> response) {
                            if(response.isSuccessful()){
                                Thongtintaikhoan thongtintaikhoan = response.body();
                                session.setTen(thongtintaikhoan.getTen());
                                session.setEmail(thongtintaikhoan.getEmail());
                                session.setId(thongtintaikhoan.getIdNguoiDung());
                            }
                        }

                        @Override
                        public void onFailure(Call<Thongtintaikhoan> call, Throwable t) {

                        }
                    });
                    if(!session.getTen().equals("")) {
                        call.enqueue(new Callback<Authenticate>() {
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
                                }
                            }

                            @Override
                            public void onFailure(Call<Authenticate> call, Throwable t) {

                            }
                        });
                    }else{
                        dialog.dismiss();
                        Toast.makeText(DangNhap.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                        session.clearSession();
                    }
                }
            }
        });
    }
}
