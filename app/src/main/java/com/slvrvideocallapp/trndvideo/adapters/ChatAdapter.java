package com.slvrvideocallapp.trndvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.databinding.ItemChatAdBinding;
import com.slvrvideocallapp.trndvideo.databinding.ItemChatBinding;
import com.slvrvideocallapp.trndvideo.models.ModelChat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CHATTYPE = 2;
    List<ModelChat> chats = new ArrayList<>();
    private Context context;
    private int tpos = 0;


    @Override
    public int getItemViewType(int position) {

            return CHATTYPE;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new ChatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ChatViewHolder viewHolder = (ChatViewHolder) holder;
            viewHolder.setData(position);
    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void addData(ModelChat modelChat) {
        chats.add(modelChat);
        notifyItemInserted(chats.size() - 1);


    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {
        ItemChatBinding binding;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemChatBinding.bind(itemView);
        }

        public void setData(int position) {
            ModelChat modelChat = chats.get(position);
            binding.tvrobot.setVisibility(View.GONE);
            binding.tvuser.setVisibility(View.GONE);
            binding.lytimagerobot.setVisibility(View.GONE);
            binding.lytimageuser.setVisibility(View.GONE);
            if (modelChat.isUser()) {
                if (!modelChat.getMessage().equals("")) {
                    binding.tvuser.setText(modelChat.getMessage());
                    binding.tvuser.setVisibility(View.VISIBLE);
                }
                if (modelChat.getImageuri() != null) {
                    binding.imageuser.setImageURI(modelChat.getImageuri());
                    binding.lytimageuser.setVisibility(View.VISIBLE);
                }

            }

            if (!modelChat.isUser()) {
                if (!modelChat.getMessage().equals("")) {
                    binding.tvrobot.setText(modelChat.getMessage());
                    binding.tvrobot.setVisibility(View.VISIBLE);
                }
                if (modelChat.getImagename() != null) {
                    binding.imagerobot.setImageResource(context.getResources().getIdentifier("raw/" + modelChat.getImagename(), null, context.getPackageName()));
                    binding.lytimagerobot.setVisibility(View.VISIBLE);
                }

            }

        }
    }
}
