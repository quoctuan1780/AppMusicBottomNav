package com.example.appmusicbotnav.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BXHAdapter;
import com.example.appmusicbotnav.adapter.SlideAdapter;
import com.example.appmusicbotnav.model.Slide;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BangXepHang extends Fragment {
    private ViewPager v_flipper;
    private View view;
    private BXHAdapter adapter;
    private BXHAdapter adapter1;
    private ListView listView;
    private ListView listViewTH;
    private SearchView searchView;
    private ArrayList<Baihat> dsbh;
    private ArrayList<Baihat> listtruyen;
    final int MY_PERMISSION_REQUEST = 1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bangxephang, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Slide slide2 = new Slide(R.drawable.mm2);
        Slide slide3 = new Slide(R.drawable.poster2);
        Slide slide4 = new Slide(R.drawable.poster4);
        ArrayList<Slide> images = new ArrayList<Slide>();

        images.add(slide2);
        images.add(slide3);
        images.add(slide4);
        v_flipper = view.findViewById(R.id.imgslide);
        SlideAdapter imgslide = new SlideAdapter(getContext(),images);
        v_flipper.setAdapter(imgslide);
        listView = view.findViewById(R.id.lv_hien_thi_top_20);
        listViewTH = view.findViewById(R.id.lv_topTH);
        dsbh = new ArrayList<>();
        dsbh=laynhac();
        v_flipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idcs = dsbh.get(v_flipper.getCurrentItem()).getIdCaSi();
                DataService dataService = APIService.getService();
                Call<List<Baihat>> ds = dataService.laybhtheoidcs();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbhtheocs", (ArrayList<? extends Parcelable>) ds);

            }
        });
        Button vn = view.findViewById(R.id.bxhVN);
        vn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20TT(dsbh);
                adapter1 = new BXHAdapter(getContext(), listtruyen);
                listView.setAdapter(adapter1);
            }
        });
        Button usuk = view.findViewById(R.id.bxhUSUK);

        usuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20POP(dsbh);
                adapter1 = new BXHAdapter(getContext(), listtruyen);
                listView.setAdapter(adapter1);
            }
        });
        Button asia = view.findViewById(R.id.bxhAsia);
        asia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20Remix(dsbh);
                adapter1 = new BXHAdapter(getContext(), listtruyen);
                listView.setAdapter(adapter1);
            }
        });
        Button kpop = view.findViewById(R.id.bxhKPOP);
        kpop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20NT(dsbh);
                adapter1 = new BXHAdapter(getContext(), listtruyen);
                listView.setAdapter(adapter1);
            }
        });
        searchView = view.findViewById(R.id.search_bxh);
        timkiem();
        listViewTH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) dsbh);
                try {
                    startActivity(phatnhac);
                }catch (Exception e){

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) dsbh);
                try {
                    startActivity(phatnhac);
                }catch (Exception e){

                }
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_layout_rank);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public ArrayList<Baihat> Top20POP(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(top20.size()<20)
            {
                if(dsbh.get(i).getTheLoai().equals( "POP"))
                    top20.add(dsbh.get(i));
            }
            else
                break;
        }

        return top20;
    }

    public ArrayList<Baihat> Top20Remix(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(top20.size()<20)
            {
                if(dsbh.get(i).getTheLoai().equals("Remix"))
                    top20.add(dsbh.get(i));
            }
            else
                break;
        }

        return top20;
    }

    public ArrayList<Baihat> Top20NT(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(top20.size()<20)
            {
                if(dsbh.get(i).getTheLoai().equals("Nhạc Trẻ") || dsbh.get(i).getTheLoai().equals("Nh?c Tr?"))
                    top20.add(dsbh.get(i));
            }
            else
                break;
        }

        return top20;
    }

    public ArrayList<Baihat> Top20TT(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(top20.size()<20)
            {
                if(dsbh.get(i).getTheLoai().equals("Trữ tình"))
                    top20.add(dsbh.get(i));
            }
            else
                break;
        }

        return top20;
    }

    private ArrayList<Baihat> laynhac() {
        try {
            DataService dataService = APIService.getService();
            Call<List<Baihat>> ds = dataService.lay100baihat();

            ds.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    if (response.isSuccessful()) {
                        dsbh = (ArrayList<Baihat>) response.body();
                        if (getContext() != null) {
                            adapter = new BXHAdapter(getContext(), dsbh);
                            listViewTH.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(getContext(), "Không tải được danh sách bài hát", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
        }

        return dsbh;
    }

    private void timkiem(){
        final ArrayList<Baihat> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BXHAdapter(getContext(), dsbh);
                    listView.setAdapter(adapter);

                    return false;
                }
                else{
                    listClone.clear();
                    for(Baihat bh : dsbh)
                        if(bh.getTenBaiHat().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(bh);
                            khoitaobaihat(listClone);

                        }
                    if(listClone.isEmpty()) khoitaobaihat(listClone);
                }
                return false;
            }
        });
    }
    private void khoitaobaihat(ArrayList<Baihat> listBh){
        adapter = new BXHAdapter(getContext(), listBh);

        listView.setAdapter(adapter);
    }

}
