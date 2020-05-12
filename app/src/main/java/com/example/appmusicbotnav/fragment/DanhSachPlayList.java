package com.example.appmusicbotnav.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.adapter.PlaylistTablayoutAdapter;
import com.google.android.material.tabs.TabLayout;

public class DanhSachPlayList extends Fragment {
    private View view;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager pager;
    public DanhSachPlayList(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        setHasOptionsMenu(true);
        toolbar = (Toolbar) view.findViewById(R.id.tb_playlist);
        toolbar.setTitle("Playlist");
        toolbar.setBackgroundColor(R.color.gray_color);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addTabs();
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            NavHostFragment.findNavController(DanhSachPlayList.this)
                    .navigate(R.id.action_frag_playlist_to_item_canhan2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        Log.i("TAG", "onStop Playlist: ");
        super.onStop();
    }

    private void addTabs() {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_playlist);
        pager = (ViewPager) view.findViewById(R.id.vp_playlist);
        pager.setAdapter(new PlaylistTablayoutAdapter(getFragmentManager()));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(pager);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
