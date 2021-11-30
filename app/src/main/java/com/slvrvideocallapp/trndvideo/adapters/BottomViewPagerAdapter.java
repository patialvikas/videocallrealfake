package com.slvrvideocallapp.trndvideo.adapters;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.models.EmojiIconRoot;
import com.slvrvideocallapp.trndvideo.models.EmojicategoryRoot;
import com.slvrvideocallapp.trndvideo.retrofit.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomViewPagerAdapter extends PagerAdapter {
    EmojiListnerViewPager emojiListnerViewPager;
    private List<EmojicategoryRoot.Datum> categories;

    public BottomViewPagerAdapter(List<EmojicategoryRoot.Datum> categories) {

        this.categories = categories;
    }

    public EmojiListnerViewPager getEmojiListnerViewPager() {
        return emojiListnerViewPager;
    }

    public void setEmojiListnerViewPager(EmojiListnerViewPager emojiListnerViewPager) {
        this.emojiListnerViewPager = emojiListnerViewPager;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_emojiviewpager, container, false);

        Log.d("TAG", "instantiateItem: " + categories.get(position).getName());
        RecyclerView recyclerView = view.findViewById(R.id.rvEmojiSheet);
        Call<EmojiIconRoot> call = RetrofitBuilder.create(container.getContext()).getEmojiByCategory(categories.get(position).get_id());
        call.enqueue(new Callback<EmojiIconRoot>() {
            @Override
            public void onResponse(Call<EmojiIconRoot> call, Response<EmojiIconRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && !response.body().getData().isEmpty()) {

                    EmojiAdapter emojiAdapter = new EmojiAdapter(response.body().getData());
                    recyclerView.setAdapter(emojiAdapter);
                    emojiAdapter.setOnEmojiClickListnear((bitmap, coin) -> emojiListnerViewPager.emojilistnerViewpager(bitmap, coin));
                }
            }

            @Override
            public void onFailure(Call<EmojiIconRoot> call, Throwable t) {
//ll
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public interface EmojiListnerViewPager {
        void emojilistnerViewpager(Bitmap bitmap, Long coin);
    }
}
