package com.example.pinterview.questions;

import static com.example.pinterview.application.PinterviewApp.dbHelper;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pinterview.data.AnswerWithQuestionEntity;
import com.example.pinterview.enumtype.AppMode;
import com.example.pinterview.data.PinterViewDataSource;
import com.example.pinterview.data.QuestionModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuestionListViewModel extends ViewModel {
    private PinterViewDataSource dataSource;

    private final MutableLiveData<String> _appMode = new MutableLiveData<>(AppMode.PRACTICE.name());
    public LiveData<String> appMode = _appMode;

    private final MutableLiveData<List<AnswerWithQuestionEntity>> _questions = new MutableLiveData<>();
    public LiveData<List<AnswerWithQuestionEntity>> questions = _questions;

    private final MutableLiveData<List<AnswerWithQuestionEntity>> _filteredQuestions = new MutableLiveData<>();
    public LiveData<List<AnswerWithQuestionEntity>> filteredQuestions = _filteredQuestions;

    public QuestionListViewModel() {

    }

    public void changeAppMode(String appMode) {
        _appMode.setValue(appMode);
    }

    public void getQuestions(String appMode) {
        dataSource = new PinterViewDataSource(dbHelper.getWritableDatabase());
        List<AnswerWithQuestionEntity> answerWithQuestionEntities;
        if(Objects.equals(appMode, AppMode.PRACTICE.name()) || Objects.equals(appMode, AppMode.EDIT.name()) ) {
            List<QuestionModel> questionList = dataSource.getAllQuestions();

            answerWithQuestionEntities = questionList.stream().map(it -> {
                AnswerWithQuestionEntity answerWithQuestion = new AnswerWithQuestionEntity();
                answerWithQuestion.setQuestion(it);
                return answerWithQuestion;
            }).collect(Collectors.toList());

            _questions.setValue(answerWithQuestionEntities);
            _filteredQuestions.setValue(answerWithQuestionEntities);
        } else if(Objects.equals(appMode, AppMode.SEARCH_ANSWER.name()) ){
            answerWithQuestionEntities = dataSource.getAnswers(null);
            _questions.setValue(answerWithQuestionEntities);
            _filteredQuestions.setValue(answerWithQuestionEntities);
        }
    }

    public Boolean getQuestionHasAnswer(Integer questionId) {
        dataSource = new PinterViewDataSource(dbHelper.getWritableDatabase());
        List<AnswerWithQuestionEntity> answers;
        answers = dataSource.getAnswers(questionId);
        return (answers != null && !answers.isEmpty());
    }

    public void filterQuestions(String query) {
        List<AnswerWithQuestionEntity> filteredList = questions.getValue()
                .stream().filter(it ->
                        it.getQuestion().getContent().contains(query) ||
                                it.getQuestion().getCompany().contains(query) ||
                                it.getQuestion().getType().contains(query)
                ).collect(Collectors.toList());
        _filteredQuestions.setValue(filteredList);
    }

    public Boolean deleteQuestion(Integer id) {
        return dataSource.deleteQuestion(id);
    }
}
