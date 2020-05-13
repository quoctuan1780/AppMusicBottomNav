package com.example.appmusicbotnav.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appmusicbotnav.R;
import com.example.appmusicbotnav.activity.PhatNhac;
import com.example.appmusicbotnav.adapter.BaiHatAdapter;
import com.example.appmusicbotnav.model.BaiHat;
import java.util.ArrayList;

public class BangXepHang extends Fragment {
    ViewFlipper v_flipper;
    private View view;
    BaiHatAdapter adapter;
    ListView listView;
    ImageView imgmenu;
    PopupMenu dropMenu;
    Menu menu;
    SearchView searchView;
    ArrayList<BaiHat> dsbh;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bangxephang, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int images[]= {R.drawable.poster1,R.drawable.poster3,R.drawable.poster2,R.drawable.poster4};
        v_flipper = view.findViewById(R.id.imgview_bhx);
        listView = view.findViewById(R.id.lv_bxh);
        for(int i = 0; i < images.length; i++)
        {
            flipperImages(images[i]);
        }
    }
    public ArrayList<BaiHat> Top100VN(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<4; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }
    public ArrayList<BaiHat> Top100USUK(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<5; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }
    public ArrayList<BaiHat> Top100Asia(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<6; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }
    public ArrayList<BaiHat> Top100KPOP(ArrayList<BaiHat> dsbh)
    {
        ArrayList<BaiHat> top100 = new ArrayList<>();
        for(int i =0; i<3; i++)
        {
            top100.add(dsbh.get(i));
        }
        return  top100;
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(view.getContext());
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval((30));
        v_flipper.setAutoStart(true);
        v_flipper.setInAnimation(view.getContext(), android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(view.getContext(), android.R.anim.slide_out_right);

    }
    private void khoitaomenu(){
        dropMenu = new PopupMenu(getContext(), imgmenu);
        menu = dropMenu.getMenu();
        dropMenu.getMenuInflater().inflate(R.menu.menu_bxh_kbeat, menu);
        dropMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download_kbeat:
                        // chưa viết luôn
                        return true;
                    case R.id.themvaoplaylist_kbeat:
                        // them vao playlist chua  viet
                        return true;
                }
                return false;
            }
        });

        imgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropMenu.show();
            }
        });
    }
        public void down()
        {
        //bla bla
        }
    private void phatnhac(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent phatnhac = new Intent(getActivity(), PhatNhac.class);
                phatnhac.putExtra("vitri", position);
                phatnhac.putExtra("list", dsbh);
                startActivity(phatnhac);
            }
        });
    }
    private void timkiem(){
        final ArrayList<BaiHat> listClone = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    adapter = new BaiHatAdapter(getContext(), dsbh);
                    listView.setAdapter(adapter);

                    return false;
                }
                else{
                    listClone.clear();
                    for(BaiHat bh : dsbh)
                        if(bh.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            listClone.add(bh);
                            khoitaobaihat(listClone);

                        }
                    if(listClone.isEmpty()) khoitaobaihat(listClone);
                }
                return false;
            }
        });
    }
    private void khoitaobaihat(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);

        listView.setAdapter(adapter);
    }
        private void hienthiBH(ArrayList<BaiHat> listBh){
        adapter = new BaiHatAdapter(getContext(), listBh);

        listView.setAdapter(adapter);
    }

}
