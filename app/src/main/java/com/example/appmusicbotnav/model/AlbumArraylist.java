package com.example.appmusicbotnav.model;

public class AlbumArraylist {
    private int idAlbum;
    private String tenAlbum;

    public AlbumArraylist(int id, String ten){
        this.idAlbum = id;
        this.tenAlbum = ten;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }

    public int getIdAlbum() {
        return idAlbum;
    }
}
