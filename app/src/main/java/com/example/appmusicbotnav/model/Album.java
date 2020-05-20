package com.example.appmusicbotnav.model;

import java.util.ArrayList;

public class Album {
    private String tenAlbum;
    ArrayList<BaiHat> listBh;

    public Album(String tenAlbum, ArrayList<BaiHat> baiHat){
        this.tenAlbum = tenAlbum;
        this.listBh = baiHat;
    }

    public ArrayList<BaiHat> getListBh() {
        return listBh;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }
}
