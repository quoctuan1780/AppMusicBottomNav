package com.example.appmusicbotnav.model;

import android.widget.Adapter;

import java.io.Serializable;

public class BaiHat implements Serializable{

    private String tenbaihat;
    private String tencasi;
    private String path;
    private int thutubai;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BaiHat(String title, String subTitle, String path) {
        this.tenbaihat = title;
        this.tencasi = subTitle;
        this.path = path;

    }

    public BaiHat(String title, String subTitle, int thutubai) {
        this.tenbaihat = title;
        this.tencasi = subTitle;
        this.thutubai = thutubai;

    }

    public String getTitle() {
        return tenbaihat;
    }

    public void setTitle(String title) {
        this.tenbaihat = title;
    }

    public String getSubTitle() {
        return tencasi;
    }

    public int getFile(){return thutubai;}

}
