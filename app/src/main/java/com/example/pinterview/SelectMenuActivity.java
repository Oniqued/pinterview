package com.example.pinterview;

import static com.example.pinterview.enumtype.AppMode.SEARCH_ANSWER;
import static com.example.pinterview.questions.QuestionListActivity.APP_MODE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pinterview.databinding.ActivitySelectMenuBinding;
import com.example.pinterview.enumtype.AppMode;
import com.example.pinterview.questions.QuestionListActivity;
import com.example.pinterview.questions.RegisterActivity;

public class SelectMenuActivity extends AppCompatActivity {
    private ActivitySelectMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        clickMenu();
    }

    private void clickMenu() {
        binding.buttonRegister.textviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(new Intent(SelectMenuActivity.this, RegisterActivity.class));}
        });
        binding.buttonEdit.textviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navToQuestionList(AppMode.EDIT);
            }
        });
        binding.buttonPractice.textviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navToQuestionList(AppMode.PRACTICE);
            }
        });
        binding.buttonCheckRecords.textviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {navToQuestionList(SEARCH_ANSWER);}
        });
    }

    private void navToQuestionList(AppMode appMode) {
        Intent intent = new Intent(SelectMenuActivity.this, QuestionListActivity.class);
        intent.putExtra(APP_MODE, appMode.name());
        startActivity(intent);
    }
}