package com.example.appmusicbotnav.modelOnline;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playlist {

    @SerializedName("idPlayList")
    @Expose
    private Integer idPlayList;
    @SerializedName("tenPlayList")
    @Expose
    private String tenPlayList;
    @SerializedName("idNguoiDung")
    @Expose
    private Integer idNguoiDung;
    @SerializedName("listBaiHat_PlayList")
    @Expose
    private List<BaiHat_Playlist> listBaiHatPlayList = null;

    public Integer getIdPlayList() {
        return idPlayList;
    }

    public void setIdPlayList(Integer idPlayList) {
        this.idPlayList = idPlayList;
    }

    public String getTenPlayList() {
        return tenPlayList;
    }

    public void setTenPlayList(String tenPlayList) {
        this.tenPlayList = tenPlayList;
    }

    public Integer getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(Integer idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public List<BaiHat_Playlist> getListBaiHatPlayList() {
        return listBaiHatPlayList;
    }

    public void setListBaiHatPlayList(List<BaiHat_Playlist> listBaiHatPlayList) {
        this.listBaiHatPlayList = listBaiHatPlayList;
    }
}

