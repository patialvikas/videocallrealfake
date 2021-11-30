package com.slvrvideocallapp.trndvideo.retrofit;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slvrvideocallapp.trndvideo.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public static RetrofitService create(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        SessionManager sessionManager = new SessionManager(context);
        String base = sessionManager.getStringValue(Const.BASE_URL);
        Log.d("TAGsss", "create: " + base);

        return new Retrofit.Builder()
                .baseUrl(base)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService.class);


    }
}
