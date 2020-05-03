package com.example.appmusicbotnav.model;

public class BaiHat {

    private String tenbaihat;
    private String tencasi;
    private String path;

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

    public String getTitle() {
        return tenbaihat;
    }

    public void setTitle(String title) {
        this.tenbaihat = title;
    }

    public String getSubTitle() {
        return tencasi;
    }

}
