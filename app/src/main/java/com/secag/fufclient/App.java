package com.secag.fufclient;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static FufApi fufApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://fuf.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fufApi = retrofit.create(FufApi.class);
    }

    public static FufApi getFufApi() {
        return fufApi;
    }
}
