package com.example.appmusicbotnav.service;

import com.example.appmusicbotnav.modelOnline.Authenticate;
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Casi;
import com.example.appmusicbotnav.modelOnline.Playlist;
import com.example.appmusicbotnav.modelOnline.Taikhoan;
import com.example.appmusicbotnav.modelOnline.Thongtintaikhoan;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DataService {
    @GET("casi/find-all")
    Call<List<Casi>> LaydulietcasiAll();

    @GET("baihat/find-random")
    Call<List<Baihat>> laybaihatGoiY();

    @PUT("baihat/change-luotnghe")
    Call<Void> tangluotthich(@Query("id") int id);

    @GET("album/find-all")
    Call<List<Album>> LaydulieuadbumAll();

    @GET("album/find-random")
    Call<List<Album>> LaydulieualbumGoiy();

    @POST("authenticate")
    Call<Authenticate> Dangnhap(@Body Taikhoan dangnhap);

    //Authorization Bearer
    @GET("playlist/find-by-id")
    Call<Object> LayPlaylist(@Header("Authorization") String token, @Query("id") int id);

    @GET("nguoidung/find-by-ten")
    Call<Thongtintaikhoan> Laythongtintaikhoan(@Query("ten") String ten);

    @GET("playlist/find-by-idnguoidung")
    Call<List<Playlist>> LayPlaylistTheotaikhoan(@Header("Authorization") String token, @Query("id") int id);

    @GET("baihat/find-bxh")
    Call<List<Baihat>> lay100baihat();

    @GET("baihat/find-by-idcasi")
    Call<List<Baihat>> laybhtheoidcs();

    @GET("baihat/find-all")
    Call<List<Baihat>> laybh();
}
