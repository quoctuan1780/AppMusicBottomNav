package com.example.appmusicbotnav.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences pref;

    public Session(Context context){
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token){
        pref.edit().putString("token", token).commit();
    }

    public String getToken(){
        String token = pref.getString("token", "");
        return token;
    }

    public void setTen(String ten){
        pref.edit().putString("ten", ten).commit();
    }

    public String getTen(){
        String ten = pref.getString("ten", "");
        return ten;
    }

    public void setEmail(String email){
        pref.edit().putString("email", email).commit();
    }

    public String getEmail(){
        String email = pref.getString("email", "");
        return email;
    }

    public int getId(){
        int id = pref.getInt("id", 0);
        return id;
    }

    public void setId(int id){
        pref.edit().putInt("id", id).commit();
    }

    public void clearSession(){
        pref.edit().clear().commit();
    }
}
