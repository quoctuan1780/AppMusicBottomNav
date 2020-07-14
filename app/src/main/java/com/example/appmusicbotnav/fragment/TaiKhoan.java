package com.example.appmusicbotnav.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.navigation.fragment.NavHostFragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.DangNhap;
import com.example.appmusicbotnav.session.Session;

public class TaiKhoan extends Fragment {
    private View view, noidungView;
    private RelativeLayout rl_noidung;
    private Button bt_dangnhapngay;
    private LinearLayout ll_doimatkhau, ll_danguxat;
    private TextView tv_name, tv_email;
    private LayoutInflater inflater;

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
        inflater = LayoutInflater.from(getContext());
        final Session session = new Session(getContext());
        if(session.getToken() != "") {
            noidungView = inflater.inflate(R.layout.item_thongtintaikhoan, null);
            ll_danguxat = (LinearLayout) noidungView.findViewById(R.id.ll_dangxuat);
            tv_name = (TextView) noidungView.findViewById(R.id.tv_name);
            tv_email = (TextView) noidungView.findViewById(R.id.tv_email);
            ll_doimatkhau = (LinearLayout) noidungView.findViewById(R.id.ll_doimatkhau);
            tv_name.setText(session.getTen());
            tv_email.setText(session.getEmail());
            rl_noidung.addView(noidungView);
            ll_doimatkhau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(getParentFragment())
                            .navigate(R.id.action_item_taikhoan_to_doiMatKhau);
                }
            });
            ll_danguxat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session.clearSession();
                    if(Online.playlistArrayList != null && DanhSachPlayListOnline.playlistArrayList != null) {
                        Online.playlistArrayList.clear();
                        DanhSachPlayListOnline.playlistArrayList.clear();
                    }
                    Toast.makeText(getContext(), "Bạn đã đăng xuất", Toast.LENGTH_LONG).show();
                    getViewDangNhap();
                }
            });
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

    public void getViewDangNhap(){
        rl_noidung.removeAllViews();
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
