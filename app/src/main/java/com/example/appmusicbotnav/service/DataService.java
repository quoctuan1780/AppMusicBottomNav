package com.example.appmusicbotnav.service;

import com.example.appmusicbotnav.modelOnline.Authenticate;
import com.example.appmusicbotnav.modelOnline.Album;
import com.example.appmusicbotnav.modelOnline.BaiHat_Playlist;
import com.example.appmusicbotnav.modelOnline.Baihat;
import com.example.appmusicbotnav.modelOnline.Casi;
import com.example.appmusicbotnav.modelOnline.Comment;
import com.example.appmusicbotnav.modelOnline.Playlist;
import com.example.appmusicbotnav.modelOnline.Taikhoan;
import com.example.appmusicbotnav.modelOnline.Thongtintaikhoan;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DataService {
    @GET("casi/find-all")
    Call<List<Casi>> LaydulietcasiAll();

    @GET("casi/find-random")
    Call<List<Casi>> LaydulietcasiRandom();

    @GET("baihat/find-random")
    Call<List<Baihat>> laybaihatGoiY();

    @GET("baihat/find-all")
    Call<List<Baihat>> laybaihatAll();

    @GET("baihat/find-by-stringlike")
    Call<List<Baihat>> timkiembaihat(@Query("tenBaiHat") String tenBh);

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
    Call<Playlist> LayPlaylist(@Header("Authorization") String token, @Query("id") int id);

    @GET("playlist/find-by-name")
    Call<Playlist> LayPlaylist(@Header("Authorization") String token, @Query("name") String name);

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
  
    @POST("playlist/add-playlist")
    Call<Playlist> themPlaylist(@Header("Authorization") String token, @Body Playlist playlist);

    @POST("baihat_playlist/add-list")
    Call<List<BaiHat_Playlist>> thembaihatvaoPlaylist(@Header("Authorization") String token, @Body List<BaiHat_Playlist> baiHatPlaylist);

    @DELETE("playlist/delete-playlist")
    Call<Void> xoaPlaylist(@Header("Authorization") String token, @Query("id") int id);

    @DELETE("baihat_playlist/delete-baihat")
    Call<Void> xoaMotBaiHatPlaylist(@Header("Authorization") String token, @Query("idPlayList") int idPlaylist, @Query("idBaiHat") int idBaihat);

    @PUT("playlist/edit-playlist")
    Call<Playlist> suaTenPlaylist(@Header("Authorization") String token, @Body Playlist playlist);

    @GET("comment/find-by-idbaihat")
    Call<List<Comment>> layComment(@Query("id") int id);

    @POST("comment/add-comment")
    Call<Comment> comment(@Header("Authorization") String token, @Body Comment comment);
}
