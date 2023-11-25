package com.example.pinterview.data;

public class AnswerWithQuestionEntity {
    AnswerModel answer;
    QuestionModel question;

    public AnswerWithQuestionEntity() {

    }

    public AnswerWithQuestionEntity(AnswerModel answerModel, QuestionModel questionModel) {
        this.answer = answerModel;
        this.question = questionModel;
    }

    public AnswerModel getAnswer() {return answer;}
    public QuestionModel getQuestion() {return question;}
    public void setAnswer(AnswerModel answer) {this.answer = answer;}
    public void setQuestion(QuestionModel question) {this.question = question;}
}
