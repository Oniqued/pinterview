package com.example.pinterview.practice;

import static com.example.pinterview.util.FileUtils.fileDateFormat;
import static com.example.pinterview.application.PinterviewApp.dbHelper;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pinterview.data.AnswerModel;
import com.example.pinterview.data.AnswerWithQuestionEntity;
import com.example.pinterview.data.PinterViewDataSource;

import java.util.Date;

public class PracticeViewModel extends ViewModel {
    PinterViewDataSource datasource = new PinterViewDataSource(dbHelper.getWritableDatabase());

    private MutableLiveData<String> _appMode = new MutableLiveData<>();
    public LiveData<String> appMode = _appMode;

    private MutableLiveData<AnswerWithQuestionEntity> _answerWithQuestion = new MutableLiveData<>();
    public LiveData<AnswerWithQuestionEntity> answerWithQuestion = _answerWithQuestion;

    private MutableLiveData<Uri> _video = new MutableLiveData<>();
    public LiveData<Uri> video = _video;

    public PracticeViewModel() {

    }

    public void changeAppMode(String appMode) {
        _appMode.setValue(appMode);
    }

    public void changeQuestion(AnswerWithQuestionEntity answerWithQuestion) {
        _answerWithQuestion.setValue(answerWithQuestion);
        if(answerWithQuestion.getAnswer() != null) {
            _video.setValue(Uri.parse(answerWithQuestion.getAnswer().getVideo()));
        }
    }

    public void changeVideoUri(Uri uri) {
        _video.setValue(uri);
    }

    public void saveVideo(Uri uri, PinterViewDataSource.DatabaseResultListener<Long> listener) {
        String date = fileDateFormat.format(new Date());
        AnswerModel answerModel = new AnswerModel(null, answerWithQuestion.getValue().getQuestion().getId(), uri.toString(), date);
        datasource.insertAnswer(answerModel, listener);
    }

    public Boolean deleteAnswer() {
        if(answerWithQuestion.getValue()!= null) {
            return datasource.deleteAnswer(answerWithQuestion.getValue().getAnswer().getId());
        } else {
            return null;
        }
    }
}
