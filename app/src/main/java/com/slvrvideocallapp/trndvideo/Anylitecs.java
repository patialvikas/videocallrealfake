package com.slvrvideocallapp.trndvideo;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.slvrvideocallapp.trndvideo.models.AnylitecsAddRoot;
import com.slvrvideocallapp.trndvideo.models.AnylitecsRemoveRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anylitecs {


    public static void addUser(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", sessionManager.getUser().get_id());
            jsonObject.addProperty("country", sessionManager.getStringValue(Const.Country));
            Call<AnylitecsAddRoot> call = RetrofitBuilder.create(context).addUser(jsonObject);
            call.enqueue(new Callback<AnylitecsAddRoot>() {
                @Override
                public void onResponse(Call<AnylitecsAddRoot> call, Response<AnylitecsAddRoot> response) {
                    if (response.code() == 200) {
                        if (response.body().getStatus() == 200) {
                            Log.d("TAGany", "onResponse: anylites send");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AnylitecsAddRoot> call, Throwable t) {
                    Log.d("TAGany", "onFailure: " + t.getMessage());
                }
            });
        }


    }

    public static void removeSesson(Context context) {
        Log.d("TAGany", "removeSesson: started");
        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            Call<AnylitecsRemoveRoot> call = RetrofitBuilder.create(context).removeUser(sessionManager.getUser().get_id());
            call.enqueue(new Callback<AnylitecsRemoveRoot>() {
                @Override
                public void onResponse(Call<AnylitecsRemoveRoot> call, Response<AnylitecsRemoveRoot> response) {
                    if (response.code() == 200) {
                        if (response.body().getStatus() == 200) {
                            Log.d("TAGany", "onResponse: anylites remove");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AnylitecsRemoveRoot> call, Throwable t) {
                    Log.d("TAGany", "onFailure: " + t.getMessage());
                }
            });
        }
    }
}
