package com.slvrvideocallapp.trndvideo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.slvrvideocallapp.trndvideo.adapters.AdapterVideos;
import com.slvrvideocallapp.trndvideo.models.AdvertisementRoot;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;
import com.slvrvideocallapp.trndvideo.models.ThumbRoot;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String WEBSITE = "WEBSITE";
    RecyclerView recyclerView;

    ProgressBar pd;
    SwipeRefreshLayout swipeRefreshLayout;
    int count = 8;
    private View view;
    private CountryRoot.Datum model;
    private AdvertisementRoot.Google google;
    private AdvertisementRoot.Facebook facebook;
    private FragmentActivity context;
    private String ownAdBannerUrl;
    private ImageView imgOwnAd;
    private String adid;
    private String ownWebUrl;
    private AdapterVideos adapterVideos;
    private LinearLayout adContainer;

    public VideoListFragment(CountryRoot.Datum model) {

        this.model = model;
        Log.d("TAGcc", "VideoListFragment: " + model.get_id());
    }

    public VideoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_list, container, false);
        recyclerView = view.findViewById(R.id.rvVideos);
        imgOwnAd = view.findViewById(R.id.imgOwnAd);
        pd = view.findViewById(R.id.pd);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);
        pd.setVisibility(View.VISIBLE);

        initMain();

    }

    private void initMain() {


        if (model != null && model.get_id() != null) {
            Call<ThumbRoot> call = RetrofitBuilder.create(getActivity()).getThumbs(model.get_id());
            call.enqueue(new Callback<ThumbRoot>() {
                @Override
                public void onResponse(Call<ThumbRoot> call, Response<ThumbRoot> response) {

                    if (response.code() == 200 && !response.body().getData().isEmpty()) {


                        adapterVideos = new AdapterVideos(model.get_id(),getContext());
                        adapterVideos.addData(response.body().getData());
                        recyclerView.setAdapter(adapterVideos);


                        Log.d("TAG", "onResponse: natvie fetch");


                        Log.d("TAG", "onResponse:ccccc  size " + response.body().getData().size());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    pd.setVisibility(View.GONE);
                    view.findViewById(R.id.pd).setVisibility(View.GONE);
                }


                @Override
                public void onFailure(Call<ThumbRoot> call, Throwable t) {
//ll
                }
            });

        }

//        }
    }







    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        initMain();

    }
}