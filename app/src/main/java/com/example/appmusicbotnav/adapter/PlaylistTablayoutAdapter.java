//package com.example.appmusicbotnav.adapter;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//import com.example.appmusicbotnav.fragment.PlayListOffline;
//import com.example.appmusicbotnav.fragment.PlayListOnline;
//
////Tạm thời chưa dùng tới
//public class PlaylistTablayoutAdapter extends FragmentStatePagerAdapter {
//    public PlaylistTablayoutAdapter(@NonNull FragmentManager fm) {
//        super(fm);
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        Fragment frag = null;
//        switch (position){
//            case 0:
//                frag = new PlayListOffline();
//                break;
//            case 1:
//                frag = new PlayListOnline();
//                break;
//        }
//        return frag;
//    }
//
//    @Override
//    public int getCount() {
//        return 1;
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        String tieude = "";
//        switch (position){
//            case 0:
//                tieude = "Playlist Offline";
//                break;
//            case 1:
//                tieude = "Playlist Online";
//                break;
//        }
//        return tieude;
//    }
//}
