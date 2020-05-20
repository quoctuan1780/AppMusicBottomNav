package com.example.appmusicbotnav.modelOnline;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album {
    @SerializedName("idAlbum")
    @Expose
    private Integer idAlbum;
    @SerializedName("soLuongBai")
    @Expose
    private Integer soLuongBai;
    @SerializedName("tenAlbum")
    @Expose
    private String tenAlbum;
    @SerializedName("listBaiHat")
    @Expose
    private List<Baihat> listBaiHat = null;
    @SerializedName("idCasi")
    @Expose
    private Integer idCasi;

    public Integer getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Integer idAlbum) {
        this.idAlbum = idAlbum;
    }

    public Integer getSoLuongBai() {
        return soLuongBai;
    }

    public void setSoLuongBai(Integer soLuongBai) {
        this.soLuongBai = soLuongBai;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }

    public void setTenAlbum(String tenAlbum) {
        this.tenAlbum = tenAlbum;
    }

    public List<Baihat> getListBaiHat() {
        return listBaiHat;
    }

    public void setListBaiHat(List<Baihat> listBaiHat) {
        this.listBaiHat = listBaiHat;
    }

    public Integer getIdCasi() {
        return idCasi;
    }

    public void setIdCasi(Integer idCasi) {
        this.idCasi = idCasi;
    }
}
