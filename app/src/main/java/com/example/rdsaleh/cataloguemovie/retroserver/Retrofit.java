package com.example.rdsaleh.cataloguemovie.retroserver;
import com.example.rdsaleh.cataloguemovie.BuildConfig;
import com.example.rdsaleh.cataloguemovie.api.BaseAPI;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static Retrofit mInstance;
    private retrofit2.Retrofit retrofit;

    private Retrofit(){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized Retrofit getInstance(){
        if(mInstance == null){
            mInstance = new Retrofit();
        }
        return mInstance;
    }

    public BaseAPI baseAPI(){
        return retrofit.create(BaseAPI.class);
    }


}
