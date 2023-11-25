package com.example.pinterview.enumtype;

public enum QuestionType {
    PERSONALITY("인적성"),
    DUTY("직무");

    public String title;
    QuestionType(String title){
        this.title = title;
    }
}
