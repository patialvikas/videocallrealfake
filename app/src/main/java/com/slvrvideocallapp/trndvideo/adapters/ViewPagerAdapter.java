package com.slvrvideocallapp.trndvideo.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.slvrvideocallapp.trndvideo.VideoListFragment;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final List<CountryRoot.Datum> data;

    public ViewPagerAdapter(@NonNull FragmentManager fm, List<CountryRoot.Datum> data) {
        super(fm);
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        CountryRoot.Datum model = data.get(position);
        Log.d("TAG", "getItem: " + position);
        Log.d("TAGcc", "getItem: " + model.get_id());
        return new VideoListFragment(model);
    }


}
