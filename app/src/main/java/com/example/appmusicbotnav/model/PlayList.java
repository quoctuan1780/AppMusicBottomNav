package com.example.appmusicbotnav.model;

import java.util.ArrayList;

public class PlayList {
    private String TenPlaylist;
    ArrayList<BaiHat> baiHat;

    public PlayList(String tenPlaylist, ArrayList<BaiHat> baiHat) {
        TenPlaylist = tenPlaylist;
        this.baiHat = baiHat;
    }

    public ArrayList<BaiHat> getBaiHat() {
        return baiHat;
    }

    public String getTenPlaylist() {
        return TenPlaylist;
    }
}
