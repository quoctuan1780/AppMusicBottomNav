package com.example.appmusicbotnav;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav_thuvien;
    private NavController nav_con_thuvien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav_thuvien = (BottomNavigationView) findViewById(R.id.nav_thuvien);
        nav_con_thuvien = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(nav_thuvien, nav_con_thuvien);

//        view = (View) findViewById(R.id.nav_host_fragment);
//        nav_trangchu = (BottomNavigationView) findViewById(R.id.nav_thuvien);
//        nav_trangchu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.item_canhan:
//
//                        return true;
//                }
//                return false;
//            }
//        });
    }
//    private void KhoiTaoDanhSachBaiHat(DanhSachBaiHatOffline baiHatOffline){
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fl_trangchu, baiHatOffline);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
//    private void hienthiFrag(ThuVien trangchu) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.cl_thuvien, trangchu);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
}
