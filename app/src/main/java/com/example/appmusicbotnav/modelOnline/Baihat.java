package com.example.appmusicbotnav.modelOnline;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Baihat implements Parcelable{
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

    protected Baihat(Parcel in) {
        if (in.readByte() == 0) {
            idBaiHat = null;
        } else {
            idBaiHat = in.readInt();
        }
        tenBaiHat = in.readString();
        tenTacGia = in.readString();
        link = in.readString();
        theLoai = in.readString();
        if (in.readByte() == 0) {
            luotNghe = null;
        } else {
            luotNghe = in.readInt();
        }
        lyrics = in.readString();
        if (in.readByte() == 0) {
            idAlbum = null;
        } else {
            idAlbum = in.readInt();
        }
        if (in.readByte() == 0) {
            idCaSi = null;
        } else {
            idCaSi = in.readInt();
        }
    }

    public static final Creator<Baihat> CREATOR = new Creator<Baihat>() {
        @Override
        public Baihat createFromParcel(Parcel in) {
            return new Baihat(in);
        }

        @Override
        public Baihat[] newArray(int size) {
            return new Baihat[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (idBaiHat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idBaiHat);
        }
        dest.writeString(tenBaiHat);
        dest.writeString(tenTacGia);
        dest.writeString(link);
        dest.writeString(theLoai);
        if (luotNghe == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(luotNghe);
        }
        dest.writeString(lyrics);
        if (idAlbum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idAlbum);
        }
        if (idCaSi == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idCaSi);
        }
    }
}
