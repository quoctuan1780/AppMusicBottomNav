package com.example.appmusicbotnav.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.modelOnline.Thongtintaikhoan;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKi extends AppCompatActivity {
    private ConstraintLayout cl_dangki;
    private EditText et_email, et_tendn, et_matkhau, et_nhaplaimatkhau;
    private Button bt_dangki;
    private CheckBox cb_dieukhoan;
    private ProgressDialog dialog;
    private TextView tv_dangnhap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);
        khoiTao();
        dangKi();
        dangNhap();
        cl_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboardFrom(DangKi.this, v);
            }
        });
    }

    private void khoiTao(){
        et_email = (EditText) findViewById(R.id.et_email);
        et_tendn = (EditText) findViewById(R.id.et_tendn);
        et_matkhau = (EditText) findViewById(R.id.et_matkhau);
        et_nhaplaimatkhau = (EditText) findViewById(R.id.et_nhaplai_matkhau);
        bt_dangki = (Button) findViewById(R.id.bt_dangki);
        cb_dieukhoan = (CheckBox) findViewById(R.id.cb_dieukhoan);
        cl_dangki = (ConstraintLayout) findViewById(R.id.cl_dangki);
        tv_dangnhap = (TextView) findViewById(R.id.tv_dangnhap);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void dangNhap(){
        tv_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKi.this, DangNhap.class);
                startActivity(intent);
            }
        });
    }

    private void dangKi(){
        bt_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(DangKi.this);
                dialog.setMessage("Đang đăng kí, xin hãy đợi 30s...");
                dialog.setCanceledOnTouchOutside(false);
                DataService dataService = APIService.getService();
                final String tendn = et_tendn.getText().toString();
                final String email = et_email.getText().toString();
                final String matkhau = et_matkhau.getText().toString();
                String nhaplai = et_nhaplaimatkhau.getText().toString();
                if(tendn.equals("") || matkhau.equals("") || nhaplai.equals("") || email.equals("")){
                    Toast.makeText(DangKi.this, "Bạn nhập thông tin chưa đầy đủ", Toast.LENGTH_SHORT).show();
                }else {
                    if (!matkhau.equals(nhaplai)) {
                        Toast.makeText(DangKi.this, "Mật khẩu nhập không khớp với nhau", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!cb_dieukhoan.isChecked()) {
                            Toast.makeText(DangKi.this, "Bạn chưa đồng ý với điều khoản của chúng tôi", Toast.LENGTH_SHORT).show();
                        } else {
                            if(isNetworkConnected()) {
                                dialog.show();
                                Call<Thongtintaikhoan> taikhoanCall = dataService.Laythongtintaikhoan(tendn);
                                taikhoanCall.enqueue(new Callback<Thongtintaikhoan>() {
                                    @Override
                                    public void onResponse(Call<Thongtintaikhoan> call, Response<Thongtintaikhoan> response) {
                                        if (response.isSuccessful() && response.code() == 200) {
                                            dialog.dismiss();
                                            Toast.makeText(DangKi.this, "Tên tài khoản đã tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Thongtintaikhoan thongtintaikhoan = new Thongtintaikhoan();
                                            thongtintaikhoan.setEmail(email);
                                            thongtintaikhoan.setTen(tendn);
                                            thongtintaikhoan.setPass(matkhau);
                                            DataService dangki = APIService.getService();
                                            Call<Thongtintaikhoan> callDangKi = dangki.dangki(thongtintaikhoan);
                                            callDangKi.enqueue(new Callback<Thongtintaikhoan>() {
                                                @Override
                                                public void onResponse(Call<Thongtintaikhoan> call, Response<Thongtintaikhoan> response) {
                                                    if (response.isSuccessful() && response.code() == 200) {
                                                        dialog.dismiss();
                                                        Toast.makeText(DangKi.this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                                                        et_email.setText("");
                                                        et_matkhau.setText("");
                                                        et_nhaplaimatkhau.setText("");
                                                        et_tendn.setText("");
                                                        cb_dieukhoan.setChecked(false);
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Thongtintaikhoan> call, Throwable t) {
                                                    call.cancel();
                                                    dialog.dismiss();
                                                    Toast.makeText(DangKi.this, "Đăng kí thất bại, kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Thongtintaikhoan> call, Throwable t) {
                                        call.cancel();
                                        dialog.dismiss();
                                        Toast.makeText(DangKi.this, "Đăng kí thất bại, kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(DangKi.this, "Kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
