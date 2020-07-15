package com.example.appmusicbotnav.service;

import com.example.appmusicbotnav.modelYoutube.ModelHome;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class YoutubeAPI {
    //AIzaSyCpcb5--BX5ht-gbCUWZLi3tBA0RFc7thA
    //AIzaSyBYPxgLAOlVBejSLLMUrC_ZDHWDApzBBc0
    public static final String BASE_URL="https://www.googleapis.com/youtube/v3/";
    public static final String KEY="AIzaSyCpcb5--BX5ht-gbCUWZLi3tBA0RFc7thA";
    public static final String sch="search?key=";
    public static final String mx="&maxResults=10";
    public static final String part="&part=snippet";
    public static final String query="&q=";
    public static final String type="&type=video";

    public interface HomeVideo{
        @GET
        Call<ModelHome> getYT(@Url String url);
    }

    private static HomeVideo homeVideo = null;
    public static HomeVideo getHomeVideo(){
        if(homeVideo == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            homeVideo = retrofit.create(HomeVideo.class);
        }
        return homeVideo;
    }
}
