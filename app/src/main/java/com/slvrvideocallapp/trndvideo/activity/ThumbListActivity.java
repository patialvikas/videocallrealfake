package com.slvrvideocallapp.trndvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.adapters.AdapterVideos;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;
import com.slvrvideocallapp.trndvideo.models.ThumbRoot;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThumbListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    ProgressBar pd;
    SwipeRefreshLayout swipeRefreshLayout;
    int count = 2;

    private CountryRoot.Datum model;
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private FragmentActivity context;
    private String ownAdBannerUrl;
    private ImageView imgOwnAd;
    private String adid;
    private String ownWebUrl;
    private AdapterVideos adapterVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb_list);

        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        if (cid != null && !cid.equals("")) {
            CountryRoot.Datum datum = new Gson().fromJson(cid, CountryRoot.Datum.class);
            if (datum != null) {
                model = datum;
                initView();
            }
        }

    }

    private void initView() {
        recyclerView = findViewById(R.id.rvVideos);
        imgOwnAd = findViewById(R.id.imgOwnAd);
        pd = findViewById(R.id.pd);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);


        pd.setVisibility(View.VISIBLE);
        initMain();
    }

    private void initMain() {


        if (model != null && model.get_id() != null) {
            Call<ThumbRoot> call = RetrofitBuilder.create(this).getThumbs(model.get_id());
            call.enqueue(new Callback<ThumbRoot>() {
                @Override
                public void onResponse(Call<ThumbRoot> call, Response<ThumbRoot> response) {

                    if (response.code() == 200 && !response.body().getData().isEmpty()) {


                        adapterVideos = new AdapterVideos(model.get_id(),ThumbListActivity.this);
                        adapterVideos.addData(response.body().getData());
                        recyclerView.setAdapter(adapterVideos);


//                        for (int i = 0; i < response.body().getData().size(); i++) {
//                            if (i != 0 && i % 2 == 0) {
                        setNativeLogic();
                        Log.d("TAG", "onResponse: natvie fetch");
//                            }
//                        }


                        Log.d("TAG", "onResponse:ccccc  size " + response.body().getData().size());
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    pd.setVisibility(View.GONE);
                    findViewById(R.id.pd).setVisibility(View.GONE);
                }

                private void setNativeLogic() {


                }

                @Override
                public void onFailure(Call<ThumbRoot> call, Throwable t) {
//ll
                }
            });

        }
    }



    @Override
    public void onRefresh() {

    }
}