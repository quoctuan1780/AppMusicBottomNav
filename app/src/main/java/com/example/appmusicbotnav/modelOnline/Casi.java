package com.example.appmusicbotnav.modelOnline;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Casi {
    @SerializedName("idCaSi")
    @Expose
    private Integer idCaSi;
    @SerializedName("tenCaSi")
    @Expose
    private String tenCaSi;
    @SerializedName("listBaiHat")
    @Expose
    private List<Baihat> listBaiHat = null;
    @SerializedName("listAlbum")
    @Expose
    private List<Album> listAlbum = null;

    public Integer getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(Integer idCaSi) {
        this.idCaSi = idCaSi;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }

    public List<Baihat> getListBaiHat() {
        return listBaiHat;
    }

    public void setListBaiHat(List<Baihat> listBaiHat) {
        this.listBaiHat = listBaiHat;
    }

    public List<Album> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(List<Album> listAlbum) {
        this.listAlbum = listAlbum;
    }

}
