package com.example.appmusicbotnav.service;

public class APIService {
    private static String base_url = "http://api-music-app.herokuapp.com/api/";

    public static DataService getService(){
        return APIRetrofitClient.getClient(base_url).create(DataService.class);
    }
}
