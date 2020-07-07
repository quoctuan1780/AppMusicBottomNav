package com.example.appmusicbotnav.modelOnline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authenticate {

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }
}