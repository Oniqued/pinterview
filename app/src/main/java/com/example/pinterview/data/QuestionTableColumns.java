package com.example.pinterview.data;

enum QuestionTableColumns {
    KeyId("id"),
    KeyContent("content"),
    KeyExpiredTime("expiredTime"),
    KeyCompany("company"),
    KeyQuestionType("type");

    public final String key;
    QuestionTableColumns(String key) {
        this.key = key;
    }
}