package com.example.appmusicbotnav.service;

import com.example.appmusicbotnav.modelOnline.Casi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    @GET("casi/find-all")
    Call<List<Casi>> Laydulietcasi();
}
