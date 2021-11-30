package com.slvrvideocallapp.trndvideo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.activity.ThumbListActivity;
import com.slvrvideocallapp.trndvideo.databinding.ItemCouteryBinding;
import com.slvrvideocallapp.trndvideo.models.CountryRoot;
import com.slvrvideocallapp.trndvideo.retrofit.Const;

import java.util.List;

public class CountrtyAdapter extends RecyclerView.Adapter<CountrtyAdapter.CountryViewHolder> {
    private List<CountryRoot.Datum> data;
    private Context context;

    public CountrtyAdapter(List<CountryRoot.Datum> data) {

        this.data = data;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coutery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

        holder.binding.tvcountryname.setText(data.get(position).getName());
        Glide.with(context).load(Const.IMAGE_URL + data.get(position).getLogo()).circleCrop().into(holder.binding.imgcountry);
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ThumbListActivity.class).putExtra("cid", new Gson().toJson(data.get(position))));

        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        ItemCouteryBinding binding;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCouteryBinding.bind(itemView);
        }
    }
}
