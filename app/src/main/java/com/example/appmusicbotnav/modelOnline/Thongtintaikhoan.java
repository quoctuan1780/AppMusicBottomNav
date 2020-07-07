package com.example.appmusicbotnav.modelOnline;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thongtintaikhoan {

    @SerializedName("idNguoiDung")
    @Expose
    private Integer idNguoiDung;
    @SerializedName("ten")
    @Expose
    private String ten;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("listPlayList")
    @Expose
    private List<Playlist> listPlayList = null;
    @SerializedName("listDownload")
    @Expose
    private List<Object> listDownload = null;

    public Integer getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(Integer idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<Playlist> getListPlayList() {
        return listPlayList;
    }

    public void setListPlayList(List<Playlist> listPlayList) {
        this.listPlayList = listPlayList;
    }

    public List<Object> getListDownload() {
        return listDownload;
    }

    public void setListDownload(List<Object> listDownload) {
        this.listDownload = listDownload;
    }
}