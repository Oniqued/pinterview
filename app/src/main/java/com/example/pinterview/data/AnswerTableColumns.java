package com.example.pinterview.data;

enum AnswerTableColumns {
    KeyId("id"),
    KeyQuestionId("questionId"),
    KeyVideo("video"),
    KeyCreatedDate("createdDate");

    public final String key;

    AnswerTableColumns(String key) {
        this.key = key;
    }
}