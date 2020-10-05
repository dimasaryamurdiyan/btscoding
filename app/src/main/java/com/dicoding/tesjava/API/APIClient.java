package com.dicoding.tesjava.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static String BASE_URL = "http://18.139.50.74:8080/";
    public static Retrofit retrofit = null;
    public static APIInterface getClient() {
        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //Creating object for our interface
        APIInterface api = retrofit.create(APIInterface.class);
        return api; // return the APIInterface object
    }
}
