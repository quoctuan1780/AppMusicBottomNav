package com.example.appmusicbotnav.modelOnline;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Baihat {
    @SerializedName("idBaiHat")
    @Expose
    private Integer idBaiHat;
    @SerializedName("tenBaiHat")
    @Expose
    private String tenBaiHat;
    @SerializedName("tenTacGia")
    @Expose
    private String tenTacGia;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("theLoai")
    @Expose
    private String theLoai;
    @SerializedName("luotNghe")
    @Expose
    private Integer luotNghe;
    @SerializedName("lyrics")
    @Expose
    private String lyrics;
    @SerializedName("idAlbum")
    @Expose
    private Integer idAlbum;
    @SerializedName("idCaSi")
    @Expose
    private Integer idCaSi;
    @SerializedName("listComment")
    @Expose
    private List<Comment> listComment = null;

    public Integer getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(Integer idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public Integer getLuotNghe() {
        return luotNghe;
    }

    public void setLuotNghe(Integer luotNghe) {
        this.luotNghe = luotNghe;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Integer getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Integer idAlbum) {
        this.idAlbum = idAlbum;
    }

    public Integer getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(Integer idCaSi) {
        this.idCaSi = idCaSi;
    }

    public List<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }
}
