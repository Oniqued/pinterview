package com.example.pinterview.data;

public class AnswerModel {
    public static final String dateFormat = "yyyy년 MM월 dd일 hh:mm";

    Integer id;
    Integer questionId;
    String video;
    String createdDate;

    public AnswerModel() {

    }

    public AnswerModel(Integer id, Integer questionId, String video, String createdDate) {
        this.id = id;
        this.questionId = questionId;
        this.video = video;
        this.createdDate = createdDate;
    }

    public Integer getId() {return id;}
    public Integer getQuestionId() {return questionId;}
    public String getVideo() {return video;}
    public String getCreatedDate() {return createdDate;}
    public void setId(Integer id) {this.id = id;}
    public void setQuestionId(Integer questionId) {this.questionId = questionId;}
    public void setVideo(String video) {this.video = video;}
    public void setCreatedDate(String createdDate) {this.createdDate = createdDate;}
}
