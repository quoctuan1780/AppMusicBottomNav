package com.example.appmusicbotnav.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.DangNhap;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import com.example.appmusicbotnav.session.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaiKhoan extends Fragment {
    private View view;
    private RelativeLayout rl_noidung;
    private Button bt_dangnhapngay;
    private LinearLayout ll_doimatkhau, ll_danguxat;
    private TextView tv_name, tv_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_taikhoan, container, false);
        rl_noidung = (RelativeLayout) view.findViewById(R.id.rl_taikhoan_noidung);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        rl_noidung.removeAllViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View noidungView;
        final Session session = new Session(getContext());
        if(session.getToken() != "") {
            noidungView = inflater.inflate(R.layout.item_thongtintaikhoan, null);
            ll_danguxat = (LinearLayout) noidungView.findViewById(R.id.ll_dangxuat);
            tv_name = (TextView) noidungView.findViewById(R.id.tv_name);
            tv_email = (TextView) noidungView.findViewById(R.id.tv_email);
//            bt_dangxuat = (Button) noidungView.findViewById(R.id.bt_dangxuat);
//            bt_lay_laylist = (Button) noidungView.findViewById(R.id.bt_lay_playlist);
            tv_name.setText(session.getTen());
            tv_email.setText(session.getEmail());
            rl_noidung.addView(noidungView);
            ll_danguxat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session.clearSession();
                    Toast.makeText(getContext(), "Bạn đã đăng xuất", Toast.LENGTH_LONG).show();
                }
            });
//            bt_dangxuat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    session.clearSession();
//                    Toast.makeText(getContext(), "Bạn đã đăng xuất", Toast.LENGTH_LONG).show();
//                }
//            });
//            bt_lay_laylist.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getPlaylist();
//                }
//            });
        }
        else {
            noidungView = inflater.inflate(R.layout.item_dangnhap, null);
            bt_dangnhapngay = (Button) noidungView.findViewById(R.id.bt_dangnhapngay);
            rl_noidung.addView(noidungView);
            bt_dangnhapngay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DangNhap.class);
                    startActivity(intent);
                }
            });
        }
    }

    private  void getPlaylist(){
        DataService dataService = APIService.getServicePlaylist();
        String token = new Session(getContext()).getToken();
        String content = "Bearer " + token;
        Call<Object> call = dataService.LayPlaylist(content, 1);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Trả về thành công", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
