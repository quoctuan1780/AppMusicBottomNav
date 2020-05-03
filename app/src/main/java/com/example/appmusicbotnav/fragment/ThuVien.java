package com.example.appmusicbotnav.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.SlideAdapter;
import com.example.appmusicbotnav.model.Slide;

import java.util.ArrayList;

public class ThuVien extends Fragment {
    private View view;
    private Toolbar toolbar;
    private ArrayList<Slide> listQuangcao;
    private ViewPager vpQuangcao;
    public ThuVien() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_trangchu, container, false);
        QuangCao();
        HienThiQuangCao();
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_thuvien);
        toolbar.setTitle("Thư viện");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ll_baihat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ThuVien.this)
                        .navigate(R.id.action_thuVien_to_danhSachBaiHatOffline2);
            }
        });
    }

    private void QuangCao() {
        listQuangcao = new ArrayList<Slide>();
        listQuangcao.add(new Slide(R.drawable.quangcao));
        listQuangcao.add(new Slide(R.drawable.quangcao));
    }

    private void HienThiQuangCao() {
        vpQuangcao = (ViewPager) view.findViewById(R.id.vp_quangcao);
        SlideAdapter adapter = new SlideAdapter(view.getContext(), listQuangcao);
        vpQuangcao.setAdapter(adapter);
    }
}
