package com.example.appmusicbotnav.service;

public class APIService {
    private static String base_url = "http://api-music-app.herokuapp.com/api/";
    private static String playlist_url = "http://api-music-app.herokuapp.com/";


    public static DataService getService(){
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }

    public static DataService getServicePlaylist(){
        return APIRetrofitClient.getClient(playlist_url).create(DataService.class);
    }
}
