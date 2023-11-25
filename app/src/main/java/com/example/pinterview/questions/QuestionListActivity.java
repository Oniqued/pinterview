package com.example.pinterview.questions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pinterview.SelectMenuActivity;
import com.example.pinterview.data.AnswerWithQuestionEntity;
import com.example.pinterview.practice.PracticeActivity;
import com.example.pinterview.util.RecyclerViewDivider;
import com.example.pinterview.data.QuestionModel;
import com.example.pinterview.databinding.ActivityQuestionListBinding;
import com.google.gson.Gson;

import java.util.List;

public class QuestionListActivity extends AppCompatActivity {
    public static final String APP_MODE = "APP_MODE";
    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    public static final String QUESTION_FOR_EDIT = "QUESTION_FOR_EDIT";
    private ActivityQuestionListBinding binding;
    private QuestionListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(QuestionListViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        getAppMode();
        setQuestionList();
        filterQuestions();
        navBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewModel.appMode.getValue() != null) {
            viewModel.getQuestions(viewModel.appMode.getValue());
        }
    }

    private void getAppMode() {
        String appMode = getIntent().getStringExtra(APP_MODE);
        viewModel.changeAppMode(appMode);
    }

    private void setQuestionList() {
        viewModel.filteredQuestions.observe(this, new Observer<List<AnswerWithQuestionEntity>>() {
            @Override
            public void onChanged(List<AnswerWithQuestionEntity> answerWithQuestionEntities) {
                binding.recyclerviewQuestions.setAdapter(createQuestionsAdapter(answerWithQuestionEntities));
            }
        });
    }

    private QuestionListAdapter createQuestionsAdapter(List<AnswerWithQuestionEntity> answerWithQuestionEntities) {
        return new QuestionListAdapter(
                answerWithQuestionEntities,
                getIntent().getStringExtra(APP_MODE),
                new QuestionListAdapter.QuestionCallback() {
                    @Override
                    public void navToVideo(AnswerWithQuestionEntity answerWithQuestionEntity) {
                        Intent intent = new Intent(QuestionListActivity.this, PracticeActivity.class);
                        intent.putExtra(APP_MODE, viewModel.appMode.getValue());
                        intent.putExtra(SELECTED_ITEM, new Gson().toJson(answerWithQuestionEntity));
                        startActivity(intent);
                    }

                    @Override
                    public void edit(AnswerWithQuestionEntity answerWithQuestionEntity) {
                        if(viewModel.getQuestionHasAnswer(answerWithQuestionEntity.getQuestion().getId())) {
                            Toast.makeText(QuestionListActivity.this, "연습 기록이 존재하는 질문은 수정할 수 없어요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(QuestionListActivity.this, RegisterActivity.class);
                            intent.putExtra(APP_MODE, viewModel.appMode.getValue());
                            String question = new Gson().toJson(answerWithQuestionEntity.getQuestion());
                            intent.putExtra(QUESTION_FOR_EDIT, question);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void delete(AnswerWithQuestionEntity answerWithQuestionEntity) {
                        if(viewModel.getQuestionHasAnswer(answerWithQuestionEntity.getQuestion().getId())) {
                            Toast.makeText(QuestionListActivity.this, "연습 기록이 존재하는 질문은 삭제할 수 없어요.", Toast.LENGTH_SHORT).show();
                        } else {
                            showDeleteDialog(answerWithQuestionEntity.getQuestion().getId());
                        }
                    }
                }
        );
    }

    private void filterQuestions() {
        binding.edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.filterQuestions(editable.toString());
            }
        });
    }

    private void showDeleteDialog(Integer questionId) {
        new DeleteDialogFragment().setDeleteCallback(new DeleteDialogFragment.DeleteCallback() {
            @Override
            public void delete() {
                viewModel.deleteQuestion(questionId);
                viewModel.getQuestions(viewModel.appMode.getValue());
            }
        }).show(getSupportFragmentManager(), "");
    }

    private void navBack() {
        binding.imageviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}