package com.example.appmusicbotnav.modelOnline;

public class Taikhoan {
    private String ten;
    private String pass;

    public Taikhoan() {
    }

    public Taikhoan(String ten, String pass) {
        this.ten = ten;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public String getTen() {
        return ten;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
