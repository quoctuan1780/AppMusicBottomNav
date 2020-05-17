package com.example.appmusicbotnav.model;

import java.io.Serializable;

public class BaiHat implements Serializable{

    private String tenbaihat;
    private String tencasi;
    private String path;
    private int albumId;
    private boolean checkBox;

    public BaiHat(String title, String subTitle, String path, int albumId) {
        this.tenbaihat = title;
        this.tencasi = subTitle;
        this.path = path;
        this.albumId = albumId;
    }

    public BaiHat(String title, String subTitle, String path) {
        this.tenbaihat = title;
        this.tencasi = subTitle;
        this.path = path;
    }

    public BaiHat(String tenbh, String tencs, String dn, boolean cb) {
        this.tenbaihat = tenbh;
        this.tencasi = tencs;
        this.path = dn;
        this.checkBox = cb;
    }

    public int getAlbumId() { return albumId; }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
