package com.example.appmusicbotnav.service;

import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Casi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    @GET("casi/find-all")
    Call<List<Casi>> LaydulietcasiAll();

    @GET("baihat/find-all")
    Call<List<Baihat>> LaydulieubaihatAll();

    @GET("album/find-all")
    Call<List<Album>> LaydulieuadbumAll();
}
