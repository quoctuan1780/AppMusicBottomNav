package com.example.appmusicbotnav.modelOnline;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Comment {
    @SerializedName("idBaiHat")
    @Expose
    private Integer idBaiHat;
    @SerializedName("idNguoiDung")
    @Expose
    private Integer idNguoiDung;
    @SerializedName("thoiDiemBinhLuan")
    @Expose
    private String thoiDiemBinhLuan;
    @SerializedName("noiDung")
    @Expose
    private String noiDung;

    public Integer getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(Integer idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public Integer getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(Integer idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getThoiDiemBinhLuan() {
        return thoiDiemBinhLuan;
    }

    public void setThoiDiemBinhLuan(String thoiDiemBinhLuan) {
        this.thoiDiemBinhLuan = thoiDiemBinhLuan;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}
