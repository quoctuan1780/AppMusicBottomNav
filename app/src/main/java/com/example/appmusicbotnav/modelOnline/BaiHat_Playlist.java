package com.example.appmusicbotnav.modelOnline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaiHat_Playlist {
    @SerializedName("idBaiHat")
    @Expose
    private int idBaiHat;
    @SerializedName("idPlayList")
    @Expose
    private int idPlayList;
    @SerializedName("baiHat")
    @Expose
    private Baihat baiHat;

    public int getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(int idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public int getIdPlayList() {
        return idPlayList;
    }

    public void setIdPlayList(int idPlayList) {
        this.idPlayList = idPlayList;
    }



    public Baihat getBaiHat() {
        return baiHat;
    }

    public void setBaiHat(Baihat baiHat) {
        this.baiHat = baiHat;
    }
}
