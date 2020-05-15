package com.example.appmusicbotnav.model;

import java.io.Serializable;

public class BaiHat implements Serializable{

    private String tenbaihat;
    private String tencasi;
    private String path;
    private int thutubai;
    private boolean checkBox;

    public BaiHat(String tenbh, String tencs, String dn, boolean cb) {
        this.tenbaihat = tenbh;
        this.tencasi = tencs;
        this.path = dn;
        this.checkBox = cb;
    }

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

    public BaiHat(String title, String subTitle, int path, boolean cb) {
        this.tenbaihat = title;
        this.tencasi = subTitle;
        this.thutubai = path;
        this.checkBox = cb;
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

    public boolean getChecked(){return checkBox;}

    public void setCheckBox(boolean cb){
        this.checkBox = cb;
    }

}
