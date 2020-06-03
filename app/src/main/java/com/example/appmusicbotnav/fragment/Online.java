package com.example.appmusicbotnav.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatOnlineAdapter;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.service.APIService;
import com.example.appmusicbotnav.service.DataService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Online extends Fragment  {
    private View view;
    private BaiHatOnlineAdapter adapter;
    private ListView lv_baihat;
    private ArrayList<Baihat> baihatArrayList;
    public Online(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        lv_baihat = (ListView) view.findViewById(R.id.lv_baihat_online);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            laybaibatAll();
            phatnhac();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
        }
    }

    public void phatnhac(){
        lv_baihat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putParcelableArrayListExtra("listonline", (ArrayList<? extends Parcelable>) baihatArrayList);
                try {
                    startActivity(phatnhac);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void laybaibatAll(){
        DataService dataService = APIService.getService();
        Call<List<Baihat>> listbaihat = dataService.LaydulieubaihatAll();
        listbaihat.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                try {
                    baihatArrayList = (ArrayList<Baihat>) response.body();
                    adapter = new BaiHatOnlineAdapter(getContext(), baihatArrayList);
                    lv_baihat.setAdapter(adapter);
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Kiểm tra lại kết nối internet", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }
}
