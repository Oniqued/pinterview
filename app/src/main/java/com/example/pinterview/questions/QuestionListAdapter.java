package com.example.pinterview.questions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pinterview.BR;
import com.example.pinterview.data.AnswerWithQuestionEntity;
import com.example.pinterview.data.QuestionModel;
import com.example.pinterview.databinding.ItemQuestionListBinding;
import com.example.pinterview.enumtype.AppMode;

import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.QuestionListViewHolder> {
    public interface QuestionCallback {
        void navToVideo(AnswerWithQuestionEntity answerWithQuestionEntity);
        void edit(AnswerWithQuestionEntity answerWithQuestionEntity);
        void delete(AnswerWithQuestionEntity answerWithQuestionEntity);
    }

    public List<AnswerWithQuestionEntity> answerWithQuestionEntities;
    private String appMode;
    private QuestionCallback questionCallback;

    public QuestionListAdapter(List<AnswerWithQuestionEntity> answerWithQuestionEntities, String appMode, QuestionCallback questionCallback) {
        this.answerWithQuestionEntities = answerWithQuestionEntities;
        this.appMode = appMode;
        this.questionCallback = questionCallback;
    }

    @NonNull
    @Override
    public QuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuestionListBinding binding = ItemQuestionListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuestionListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListViewHolder holder, int position) {
        AnswerWithQuestionEntity answerWithQuestionEntity = answerWithQuestionEntities.get(position);
        holder.binding.setVariable(BR.answerWithQuestion, answerWithQuestionEntity);
        if(appMode.equals(AppMode.EDIT.name())) {
            holder.binding.layoutButtons.setVisibility(View.VISIBLE);
        } else {
            holder.binding.layoutButtons.setVisibility(View.GONE);
        }

        if(appMode.equals(AppMode.PRACTICE.name()) || appMode.equals(AppMode.SEARCH_ANSWER.name()) ) {
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionCallback.navToVideo(answerWithQuestionEntity);
                }
            });
        }

        holder.binding.textviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionCallback.delete(answerWithQuestionEntity);
            }
        });

        holder.binding.textviewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionCallback.edit(answerWithQuestionEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerWithQuestionEntities.size();
    }

    public class QuestionListViewHolder extends RecyclerView.ViewHolder {
        ItemQuestionListBinding binding;
        public QuestionListViewHolder(ItemQuestionListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
