package com.slvrvideocallapp.trndvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.databinding.ItemCommentBinding;
import com.slvrvideocallapp.trndvideo.models.CommentRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    Random random = new Random();
    private Context context;
    private List<CommentRoot.Datum> data = new ArrayList<>();

    public CommentAdapter() {
//ll

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {


        holder.binding.tvcomment.setText(data.get(position).getComment());
        holder.binding.tvName.setText(data.get(position).getName());
        setrandomColor(holder.binding.tvName);
    }

    private void setrandomColor(TextView tvName) {

        int i = random.nextInt(8);
        switch (i) {
            case 0:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.cgreen));
                break;
            case 1:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.csky));
                break;
            case 2:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.cpink));
                break;
            case 3:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.cyellow));
                break;
            case 4:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.purplepink));
                break;
            case 5:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.cgreen2));
                break;
            case 6:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.cred));
                break;
            case 7:
                tvName.setTextColor(ContextCompat.getColor(context, R.color.purpleblue));
                break;
            default:

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void additem(CommentRoot.Datum cmt) {

        data.add(cmt);

        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentBinding.bind(itemView);
        }
    }
}
