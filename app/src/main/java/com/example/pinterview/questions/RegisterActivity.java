package com.example.pinterview.questions;

import static com.example.pinterview.application.PinterviewApp.dbHelper;
import static com.example.pinterview.questions.QuestionListActivity.APP_MODE;
import static com.example.pinterview.questions.QuestionListActivity.QUESTION_FOR_EDIT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pinterview.R;
import com.example.pinterview.data.PinterViewDataSource;
import com.example.pinterview.data.QuestionModel;
import com.example.pinterview.databinding.ActivityRegisterBinding;
import com.example.pinterview.enumtype.AppMode;
import com.example.pinterview.enumtype.QuestionType;
import com.google.gson.Gson;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    PinterViewDataSource datasource = new PinterViewDataSource(dbHelper.getWritableDatabase());
    Integer questionId;
    String selectedType;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getQuestionInfo();
        updateInputs();
        register();
        navBack();
    }

    private void getQuestionInfo() {
        mode = getIntent().getStringExtra(APP_MODE);
        String questionJson = getIntent().getStringExtra(QUESTION_FOR_EDIT);
        if(questionJson != null && !questionJson.isEmpty()) {
            QuestionModel question = new Gson().fromJson(questionJson, QuestionModel.class);

            questionId = question.getId();
            binding.edittextExpiredTime.setText(String.valueOf(question.getExpiredTime()));
            binding.edittextQuestion.setText(question.getContent());
            binding.edittextCompanyName.setText(question.getCompany());
            selectedType = question.getType();
            if(Objects.equals(question.getType(), QuestionType.DUTY.title)) {
                binding.buttonDuty.setChecked(true);
            } else {
                binding.buttonPersonality.setChecked(true);
            }
        }
    }

    private void updateInputs() {
        binding.edittextExpiredTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    binding.textviewTimeUnit.setVisibility(View.VISIBLE);
                } else {
                    binding.textviewTimeUnit.setVisibility(View.GONE);
                }
            }
        });
        binding.radiogroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.button_personality) {
                    selectedType = QuestionType.PERSONALITY.title;
                } else {
                    selectedType = QuestionType.DUTY.title;
                }
            }
        });
    }

    private void register() {
        binding.textviewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuestion();
            }
        });
    }

    private void saveQuestion() {
        QuestionModel questionModel = new QuestionModel(
                questionId,
                binding.edittextQuestion.getText().toString(),
                Integer.valueOf(binding.edittextExpiredTime.getText().toString()),
                binding.edittextCompanyName.getText().toString(),
                selectedType
        );

        if(Objects.equals(mode, AppMode.EDIT.name())) {
            Boolean result = datasource.updateQuestion(questionModel);
            if(result) {
                Toast.makeText(RegisterActivity.this, "질문을 수정했어요!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "일시적인 오류로 질문을 수정하지 못했어요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            insertQuestion(questionModel);
        }
    }

    private void insertQuestion(QuestionModel questionModel) {
        datasource.insertQuestion(questionModel, new PinterViewDataSource.DatabaseResultListener<Long>() {
            @Override
            public void onSuccess(Long data) {
                Toast.makeText(RegisterActivity.this, "질문을 저장했습니다!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure() {
                Toast.makeText(RegisterActivity.this, "일시적인 오류로 질문을 저장하지 못했어요.", Toast.LENGTH_SHORT).show();
            }
        });
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