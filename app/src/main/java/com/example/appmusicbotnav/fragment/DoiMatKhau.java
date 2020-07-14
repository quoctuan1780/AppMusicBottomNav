package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.modelOnline.Thongtintaikhoan;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.session.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoiMatKhau extends Fragment {

    private View view;
    private Toolbar toolbar;
    private EditText et_makhaucu, et_matkhaumoi, et_nhaplaimatkhau;
    private Button bt_doimatkhau;
    private ProgressDialog dialog;
    private Session session;
    private TextView textView;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doimatkhau, container, false);
        toolbar = (Toolbar)  view.findViewById(R.id.tb_doimatkhau);
        toolbar.setBackgroundColor(R.color.gray_color);
        et_makhaucu = (EditText) view.findViewById(R.id.et_matkhaucu);
        et_matkhaumoi = (EditText) view.findViewById(R.id.et_matkhaumoi);
        et_nhaplaimatkhau = (EditText) view.findViewById(R.id.et_nhaplaimatkhau);
        bt_doimatkhau = (Button) view.findViewById(R.id.bt_doimatkhau);
        textView = (TextView) view.findViewById(R.id.tv_name_doimatkhau);
        session = new Session(getContext());
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView.setText(session.getTen());
        doiMatKhau();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void doiMatKhau(){
        bt_doimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(getContext());
                dialog.setMessage("Đang đổi mật khẩu, xin hãy đợi 30s...");
                if(et_makhaucu.getText().toString().equals("")||et_matkhaumoi.getText().toString()
                        .equals("")||et_nhaplaimatkhau.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Bạn chưa nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if(!et_matkhaumoi.getText().toString().equals(et_nhaplaimatkhau.getText().toString())) {
                        Toast.makeText(getContext(), "Mật khẩu không khớp với nhau", Toast.LENGTH_SHORT).show();
                    }else{
                        if(!et_makhaucu.getText().toString().equals(session.getPass())){
                            Toast.makeText(getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        }else{
                            final Thongtintaikhoan thongtintaikhoan = new Thongtintaikhoan();
                            thongtintaikhoan.setTen(session.getTen());
                            thongtintaikhoan.setEmail(session.getEmail());
                            thongtintaikhoan.setPass(et_matkhaumoi.getText().toString());
                            thongtintaikhoan.setIdNguoiDung(session.getId());
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                            DataService dataService = APIService.getService();
                            Call<Thongtintaikhoan> thongtintaikhoanCall = dataService.doimatkhau(thongtintaikhoan);
                            thongtintaikhoanCall.enqueue(new Callback<Thongtintaikhoan>() {
                                @Override
                                public void onResponse(Call<Thongtintaikhoan> call, Response<Thongtintaikhoan> response) {
                                    if(response.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        session.setPass(thongtintaikhoan.getPass());
                                        getActivity().onBackPressed();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Thongtintaikhoan> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Có lỗi trong quá trình đổi mật khẩu, kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
