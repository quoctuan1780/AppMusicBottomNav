package com.example.appmusicbotnav.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
    private BXHAdapter adapter;
    private BXHAdapter adapterTT;
    private BXHAdapter adapterNT;
    private BXHAdapter adapterpop;
    private BXHAdapter adapterremix;
    private ListView listViewTH;
    private SearchView searchView;
    private ArrayList<Baihat> dsbh;
    private ArrayList<Baihat> listtruyen;
    ImageView imTT;
    ListView tvTT;
    ImageView imNT;
    ListView tvNT;
    ImageView impop;
    ListView tvpop;
    ImageView imremix;
    ListView tvremix;
    ArrayList<Baihat> bhall;
    final int MY_PERMISSION_REQUEST = 1;

    private  ArrayList<Baihat> topPOP = new ArrayList<>();


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
        bhall = new ArrayList<>();
        bhall = laynhacall();

        images.add(slide2);
        images.add(slide3);
        images.add(slide4);
        v_flipper = view.findViewById(R.id.imgslide);
        SlideAdapter imgslide = new SlideAdapter(getContext(), images);
        v_flipper.setAdapter(imgslide);

        listViewTH = view.findViewById(R.id.lv_topTH);

        dsbh = new ArrayList<>();
        laynhac();

        adapter = new BXHAdapter(getContext(), dsbh);
        listViewTH.setAdapter(adapter);

        imTT = (ImageView) view.findViewById(R.id.iv_top_TT);
        tvTT = (ListView) view.findViewById(R.id.lv_top_TT);
        imNT = (ImageView) view.findViewById(R.id.iv_top_NT);
        tvNT = (ListView) view.findViewById(R.id.lv_top_NT);
        impop = (ImageView) view.findViewById(R.id.iv_top_pop);
        tvpop = (ListView) view.findViewById(R.id.lv_top_pop);
        imremix = (ImageView) view.findViewById(R.id.iv_top_remix);
        tvremix = (ListView) view.findViewById(R.id.lv_top_remix);

        ArrayList<Baihat> topTT = new ArrayList<>();
        ArrayList<Baihat> topNT = new ArrayList<>();

        ArrayList<Baihat> topRemix = new ArrayList<>();
        //sapxepgiamdan();
        if(Top20TT(dsbh).size()<1)
        {
//            for(int i =0; i< 3;i++)
//            {
//                topTT.add(Top20TT(bhall).get(i));
//            }
        }
        else {
            for(int i =0; i<Top20TT(dsbh).size() ;i++)
            {
                if(topTT.size()<3)
                topTT.add(Top20TT(dsbh).get(i));
                else
                    break;
            }
        }
        adapterTT= new BXHAdapter(getContext(), topTT);
        tvTT.setAdapter(adapterTT);
        if(Top20NT(dsbh).size()<1)
        {
//            for(int i =0; i< 3;i++)
//            {
//                topNT.add(Top20NT(bhall).get(i));
//            }
        }
        else {
            for(int i =0; i< Top20NT(dsbh).size();i++)
            {
                if(topNT.size()<3)
                topNT.add(Top20NT(dsbh).get(i));
                else
                    break;
            }
        }
        adapterNT= new BXHAdapter(getContext(), topNT);
        tvNT.setAdapter(adapterNT);

        Log.i("BAIHATBXH", Integer.toString(topPOP.size()));
        ArrayList<Baihat> top3POP = new ArrayList<>();
        if(topPOP.size() < 3)
        {
//            for(int i =0; i< 3;i++)
//            {
//                topPOP.add(Top20POP(bhall).get(i));
//            }
        }
        else {
            for(int i =0; i< topPOP.size();i++)
            {
                Log.i("BAIHATBXH", topPOP.get(i).getTenBaiHat());
                if(top3POP.size()<3)
                    top3POP.add(topPOP.get(i));
                else
                    break;
            }
        }
        adapterpop= new BXHAdapter(getContext(), top3POP);
        tvpop.setAdapter(adapterpop);

        if(Top20Remix(dsbh).size() < 1)
        {
//            for(int i =0; i< 3;i++)
//            {
//                topRemix.add(Top20Remix(bhall).get(i));
//            }
        }
        else {
            for(int i =0; i< Top20Remix(dsbh).size();i++)
            {
                if(topRemix.size()<3)
                topRemix.add(Top20Remix(dsbh).get(i));
                else
                    break;
            }
        }
        adapterremix= new BXHAdapter(getContext(), topRemix);
        tvremix.setAdapter(adapterremix);
        imTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20TT(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline2, bundle);
            }
        });

        imNT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20NT(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline2, bundle);
            }
        });
        impop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20POP(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline2, bundle);
            }
        });
        imremix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtruyen = Top20Remix(dsbh);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listtruyen);
                NavHostFragment.findNavController(BangXepHang.this)
                        .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline2, bundle);
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

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_layout_rank);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
    }


    private void laynhac(){
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
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public ArrayList<Baihat> Top20POP(final ArrayList<Baihat> dsbhtruyen) {
        ArrayList<Baihat> top20 = new ArrayList<>();
        for (int i = 0; i < dsbhtruyen.size(); i++) {
            if(top20.size() < 20)
            {
                if(dsbhtruyen.get(i).getTheLoai().equals( "POP")) {
                    Log.i("BAIHATBXH", dsbhtruyen.get(i).getTenBaiHat());
                    top20.add(dsbhtruyen.get(i));
                }
            }
            else
                break;
        }

        return top20;
    }

    public void Top20POPSua() {
        for (int i = 0; i < dsbh.size(); i++) {
            if(topPOP.size()<20)
            {
                if(dsbh.get(i).getTheLoai().equals( "POP"))
                    topPOP.add(dsbh.get(i));
            }
            else
                break;
        }
    }

    public ArrayList<Baihat> Top20Remix(ArrayList<Baihat> dsbh) {
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

    private ArrayList<Baihat> laynhacall() {
        try {
            DataService dataService = APIService.getService();
            Call<List<Baihat>> ds = dataService.laybh();

            ds.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    if (response.isSuccessful()) {
                        bhall = (ArrayList<Baihat>) response.body();
//                        if (getContext() != null) {
//                            adapter = new BXHAdapter(getContext(), dsbh);
//                            listViewTH.setAdapter(adapter);
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "Không tải được danh sách bài hát", Toast.LENGTH_LONG).show();
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
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) bhall);
                    NavHostFragment.findNavController(BangXepHang.this)
                            .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline2, bundle);

                    return false;
                }
                else{
                    listClone.clear();
                    for(Baihat bh : bhall)
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
    private void sapxepgiamdan(){
        Collections.sort(bhall, new Comparator<Baihat>() {
            @Override
            public int compare(Baihat o1, Baihat o2) {
                return o2.getTenBaiHat().compareTo(o1.getTenBaiHat());
            }
        });
    }
    private void khoitaobaihat(ArrayList<Baihat> listBh){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) listBh);
        NavHostFragment.findNavController(BangXepHang.this)
                .navigate(R.id.action_item_bxh_to_danhsachbaihatOnline2, bundle);
    }
}
