package com.slvrvideocallapp.trndvideo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.slvrvideocallapp.trndvideo.R;
import com.slvrvideocallapp.trndvideo.activity.ChatActivity;
import com.slvrvideocallapp.trndvideo.databinding.ItemQuestionsBinding;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<ChatActivity.Questions> questions;
    private OnQuestionClickListner onQuestionClickListner;

    public QuestionAdapter(List<ChatActivity.Questions> questions, OnQuestionClickListner onQuestionClickListner) {

        this.questions = questions;
        this.onQuestionClickListner = onQuestionClickListner;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.binding.tvquestion.setText(questions.get(position).getQuestion());
        holder.binding.tvquestion.setOnClickListener(v -> onQuestionClickListner.onQuestionClick(questions.get(position)));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public interface OnQuestionClickListner {
        void onQuestionClick(ChatActivity.Questions questions);
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        ItemQuestionsBinding binding;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemQuestionsBinding.bind(itemView);
        }
    }
}
