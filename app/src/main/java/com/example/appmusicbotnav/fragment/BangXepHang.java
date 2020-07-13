package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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
    private BXHAdapter adapter, adapterTT, adapterNT, adapterpop, adapterremix;
    private static ArrayList<Baihat> dsbh, listTimKiem;
    private ArrayList<Baihat> listtruyen;
    private ImageView imTT, imNT, impop, imremix, ib_timkiem;
    private ListView lvTT, lvNT, lvpop, lvremix, listViewTH;
    private  ArrayList<Baihat> topPOP, topTT, topNT, topRemix;
    private RelativeLayout relativeLayout;
    private ScrollView scrollView;
    private Toolbar toolbar;
    private EditText et_timkiem;


    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bangxephang, container, false);
        imTT = (ImageView) view.findViewById(R.id.iv_top_TT);
        lvTT = (ListView) view.findViewById(R.id.lv_top_TT);
        imNT = (ImageView) view.findViewById(R.id.iv_top_NT);
        lvNT = (ListView) view.findViewById(R.id.lv_top_NT);
        impop = (ImageView) view.findViewById(R.id.iv_top_pop);
        lvpop = (ListView) view.findViewById(R.id.lv_top_pop);
        imremix = (ImageView) view.findViewById(R.id.iv_top_remix);
        lvremix = (ListView) view.findViewById(R.id.lv_top_remix);
        listViewTH = view.findViewById(R.id.lv_topTH);
        v_flipper = view.findViewById(R.id.imgslide);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_layout_rank);
        et_timkiem = (EditText) view.findViewById(R.id.et_timkiem_bxh);
        ib_timkiem = (ImageButton) view.findViewById(R.id.ib_timkiem_bxh);
        toolbar = (Toolbar) view.findViewById(R.id.tb_bangxephang);
        toolbar.setBackgroundColor(R.color.gray_color);
        scrollView = (ScrollView) view.findViewById(R.id.sc_bangxephang);
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

        ArrayList<Slide> images = new ArrayList<>();
        images.add(new Slide(R.drawable.mm2));
        images.add(new Slide(R.drawable.poster2));
        images.add(new Slide(R.drawable.poster4));
        SlideAdapter imgSlideAdapter = new SlideAdapter(getContext(), images);
        v_flipper.setAdapter(imgSlideAdapter);

        if(dsbh == null) {
            laynhac();
        }else{
            adapter = new BXHAdapter(getContext(), dsbh);
            listViewTH.setAdapter(adapter);
            topPOP = top20POP(dsbh);
            layTop3POP(topPOP);
            topRemix = top20Remix(dsbh);
            layTop3Remix(topRemix);
            topNT = top20NT(dsbh);
            layTop3NhacTre(topNT);
            topTT = top20TT(dsbh);
            layTop3NhacTruTinh(topTT);
        }

        cuonListview();

        imTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = top20TT(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline1, bundle);
            }
        });

        imNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = top20NT(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline1, bundle);
            }
        });
        impop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = top20POP(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline1, bundle);
            }
        });
        imremix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = top20Remix(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline1, bundle);
            }
        });

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
        timkiem();

    }


    private void laynhac(){
        try {
            DataService dataService = APIService.getService();
            final Call<List<Baihat>> ds = dataService.lay100baihat();
            ds.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    if (response.isSuccessful()) {
                        dsbh = (ArrayList<Baihat>) response.body();
                        if (getContext() != null) {

                            adapter = new BXHAdapter(getContext(), top20BaiHat(dsbh));
                            listViewTH.setAdapter(adapter);
                            topPOP = top20POP(dsbh);
                            layTop3POP(topPOP);
                            topRemix = top20Remix(dsbh);
                            layTop3Remix(topRemix);
                            topNT = top20NT(dsbh);
                            layTop3NhacTre(topNT);
                            topTT = top20TT(dsbh);
                            layTop3NhacTruTinh(topTT);
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
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private ArrayList<Baihat> top20BaiHat(ArrayList<Baihat> baihatArrayList){
        ArrayList<Baihat> top20 = new ArrayList<>();
        for(int i = 0; i < baihatArrayList.size(); i++){
            if(top20.size() < 20){
                top20.add(baihatArrayList.get(i));
            }
        }
        return top20;
    }

    public ArrayList<Baihat> top20POP(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> topPOP = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(topPOP.size() < 20)
            {
                if(dsbh.get(i).getTheLoai().equals( "POP"))
                    topPOP.add(dsbh.get(i));
            }
            else
                break;
        }
        return topPOP;
    }

    private void layTop3POP(ArrayList<Baihat> topPOP){
        final ArrayList<Baihat> top3POP = new ArrayList<>();
        if(topPOP.size() < 3)
        {
            for(int i = 0; i < topPOP.size();i++)
            {
                top3POP.add(topPOP.get(i));
            }
        }
        else {
            for(int i = 0; i < topPOP.size();i++)
            {
                if(top3POP.size() < 3)
                    top3POP.add(topPOP.get(i));
                else
                    break;
            }
        }
        if(top3POP.size() > 0){
            adapterpop = new BXHAdapter(getContext(), top3POP);
            lvpop.setAdapter(adapterpop);
            lvpop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                    phatnhac.putExtra("vitri", position);
                    phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) top3POP);
                    try {
                        startActivity(phatnhac);
                    }catch (Exception e){

                    }
                }
            });
        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, Collections.singletonList("Không có bài hát nào"));
            lvpop.setAdapter(adapter);
        }
    }

    public ArrayList<Baihat> top20Remix(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(top20.size() < 20)
            {
                if(dsbh.get(i).getTheLoai().equals("Remix"))
                    top20.add(dsbh.get(i));
            }
            else
                break;
        }
        return top20;
    }

    private void layTop3Remix(ArrayList<Baihat> top20Remix){
        final ArrayList<Baihat> top3Remix = new ArrayList<>();
        if(top20Remix.size() < 3)
        {
            for(int i = 0; i < top20Remix.size();i++)
            {
                top3Remix.add(top20Remix.get(i));
            }
        }
        else {
            for(int i =0; i < top20Remix.size();i++)
            {
                if(top3Remix.size() < 3)
                    top3Remix.add(top20Remix.get(i));
                else
                    break;
            }
        }
        if(top3Remix.size() > 0) {
            adapterremix = new BXHAdapter(getContext(), top3Remix);
            lvremix.setAdapter(adapterremix);
            lvremix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                    phatnhac.putExtra("vitri", position);
                    phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) top3Remix);
                    try {
                        startActivity(phatnhac);
                    }catch (Exception e){

                    }
                }
            });
        }else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, Collections.singletonList("Không có bài hát nào"));
            lvremix.setAdapter(adapter);
        }
    }

    public ArrayList<Baihat> top20NT(ArrayList<Baihat> dsbh) {
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

    private void layTop3NhacTre(ArrayList<Baihat> top20NhacTre){
        final ArrayList<Baihat> top3NhacTre = new ArrayList<>();
        if(top20NhacTre.size() < 3)
        {
            for(int i = 0; i < top20NhacTre.size();i++)
            {
                top3NhacTre.add(top20NhacTre.get(i));
            }
        }
        else {
            for(int i = 0; i < top20NhacTre.size();i++)
            {
                if(top3NhacTre.size() < 3)
                    top3NhacTre.add(top20NhacTre.get(i));
                else
                    break;
            }
        }
        if(top3NhacTre.size() > 0) {
            adapterNT = new BXHAdapter(getContext(), top3NhacTre);
            lvNT.setAdapter(adapterNT);
            lvNT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                    phatnhac.putExtra("vitri", position);
                    phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) top3NhacTre);
                    try {
                        startActivity(phatnhac);
                    }catch (Exception e){

                    }
                }
            });
        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, Collections.singletonList("Không có bài hát nào"));
            lvNT.setAdapter(adapter);
        }
    }

    public ArrayList<Baihat> top20TT(ArrayList<Baihat> dsbh) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbh.size(); i++) {
            if(top20.size() < 20)
            {
                if(dsbh.get(i).getTheLoai().equals("Trữ tình"))
                    top20.add(dsbh.get(i));
            }
            else
                break;
        }

        return top20;
    }

    private void layTop3NhacTruTinh(ArrayList<Baihat> top20NhacTruTinh){
        final ArrayList<Baihat> top3NhacTruTinh = new ArrayList<>();
        if(top20NhacTruTinh.size() < 3)
        {
            for(int i = 0; i < top20NhacTruTinh.size();i++)
            {
                top3NhacTruTinh.add(top20NhacTruTinh.get(i));
            }
        }
        else {
            for(int i = 0; i < top20NhacTruTinh.size();i++)
            {
                if(top3NhacTruTinh.size() < 3)
                    top3NhacTruTinh.add(top20NhacTruTinh.get(i));
                else
                    break;
            }
        }
        if(top3NhacTruTinh.size() > 0) {
            adapterTT = new BXHAdapter(getContext(), top3NhacTruTinh);
            lvTT.setAdapter(adapterTT);
            lvTT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                    phatnhac.putExtra("vitri", position);
                    phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) top3NhacTruTinh);
                    try {
                        startActivity(phatnhac);
                    }catch (Exception e){

                    }
                }
            });
        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, Collections.singletonList("Không có bài hát nào"));
            lvTT.setAdapter(adapter);
        }
    }

    private void cuonListview(){
        lvpop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lvremix.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lvNT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lvTT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
    }

    private void sapxepgiamdan(ArrayList<Baihat> baihatArrayList){
        Collections.sort(baihatArrayList, new Comparator<Baihat>() {
            @Override
            public int compare(Baihat o1, Baihat o2) {
                return o2.getLuotNghe().toString().compareTo(o1.getLuotNghe().toString());
            }
        });
    }

    private void layBaiHatTimKiem(String query) {
        try {
            DataService dataService = APIService.getService();
            Call<List<Baihat>> dsTimKiem = dataService.timkiembaihat(query);
            dsTimKiem.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        listTimKiem = (ArrayList<Baihat>) response.body();
                   }
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
        }
    }

    private void timkiem(){
        final int[] count = {0};
        ib_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                if (count[0] > 1) {
                    Toast.makeText(getContext(), "Bạn đang nhấn quá nhanh", Toast.LENGTH_SHORT).show();
                } else {
                    if (et_timkiem.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Hãy nhập bài hát bạn muốn tìm kiếm", Toast.LENGTH_SHORT).show();
                    } else {
                        layBaiHatTimKiem(et_timkiem.getText().toString());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                khoitaobaihat(listTimKiem);
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    private void khoitaobaihat(ArrayList<Baihat> listBh){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listBh);
        NavHostFragment.findNavController(BangXepHang.this)
                .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline1, bundle);

    }
}
