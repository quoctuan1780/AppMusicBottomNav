package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.CasiOnlineAdapter;
import com.example.appmusicbotnav.modelOnline.Casi;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachCaSiOnline extends Fragment {
    private View view;
    private Toolbar toolbar;
    private static ArrayList<Casi> casiArrayList;
    private CasiOnlineAdapter adapter;
    private ListView lv_casi_online_all;
    private SearchView sv_casionline;
    private ArrayList<Casi> casiArrayListClone = new ArrayList<>();
    private RelativeLayout relativeLayout;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_casi_online_all, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.tb_casi_online_all);
        lv_casi_online_all = (ListView) view.findViewById(R.id.lv_casi_online_all);
        sv_casionline = (SearchView) view.findViewById(R.id.sv_timkiem_casi_online);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_casionline);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Danh sách ca sĩ");
        toolbar.setBackgroundColor(R.color.gray_color);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardFrom(getContext(), v);
                return false;
            }
        });
        if(casiArrayList !=  null){
            adapter = new CasiOnlineAdapter(getContext(), casiArrayList);
            lv_casi_online_all.setAdapter(adapter);
            timkiemcasi();
            laydanhsachbaihat();
        }else {
            try {
                laycasiAll();
                timkiemcasi();
                laydanhsachbaihat();
            } catch (Exception e) {

            }
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void timkiemcasi() {
        sv_casionline.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(casiArrayList != null) {
                    if (newText.equals("")) {
                        casiArrayListClone.clear();
                        adapter = new CasiOnlineAdapter(getContext(), casiArrayList);
                        lv_casi_online_all.setAdapter(adapter);
                        return false;
                    } else {
                        casiArrayListClone.clear();
                        for (Casi cs : casiArrayList) {
                            if (cs.getTenCaSi().toLowerCase().contains(newText.toLowerCase())) {
                                casiArrayListClone.add(cs);
                            }
                        }
                        adapter = new CasiOnlineAdapter(getContext(), casiArrayListClone);
                        lv_casi_online_all.setAdapter(adapter);
                        return false;
                    }
                }else{
                    Toast.makeText(getContext(), "Không tải được danh sách bài hát để tìm kiếm", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void laydanhsachbaihat() {
        lv_casi_online_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Casi casihientai = adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("listbh", (ArrayList<? extends Parcelable>) casihientai.getListBaiHat());
                NavHostFragment.findNavController(DanhSachCaSiOnline.this)
                        .navigate(R.id.action_danhSachCaSiOnline_to_danhsachbaihatOnline, bundle);
            }
        });
    }

    private void laycasiAll() {
        try{
            DataService dataService = APIService.getService();
            Call<List<Casi>> listCall = dataService.LaydulietcasiAll();
            listCall.enqueue(new Callback<List<Casi>>() {
                @Override
                public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                    if(response.isSuccessful()) {
                        casiArrayList = (ArrayList<Casi>) response.body();
                        if(getContext() != null) {
                            adapter = new CasiOnlineAdapter(getContext(), casiArrayList);
                            lv_casi_online_all.setAdapter(adapter);
                        }
                    }else{
                        Toast.makeText(getContext(), "Không tải được danh sách ca sĩ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }catch(Exception e){

        }
    }


}
