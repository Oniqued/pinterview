package com.example.pinterview.data;

import static com.example.pinterview.application.PinterviewApp.dbHelper;
import static com.example.pinterview.data.DBHelper.ANSWER_TABLE_NAME;
import static com.example.pinterview.data.DBHelper.QUESTION_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PinterViewDataSource {
    SQLiteDatabase database;

    public PinterViewDataSource(SQLiteDatabase database) {
        this.database = database;
    }

    public interface DatabaseResultListener <T> {
        void onSuccess(T data);
        void onFailure();
    }

    public void insertQuestion(QuestionModel questionModel, DatabaseResultListener<Long> resultListener) {
        try {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(QuestionTableColumns.KeyContent.key, questionModel.getContent());
            contentValues.put(QuestionTableColumns.KeyExpiredTime.key, questionModel.getExpiredTime());
            contentValues.put(QuestionTableColumns.KeyCompany.key, questionModel.getCompany());
            contentValues.put(QuestionTableColumns.KeyQuestionType.key, questionModel.getType());

            if(database != null) {
                Long rowIdx = database.insert(QUESTION_TABLE_NAME, null, contentValues);
                if (rowIdx != -1) {
                    resultListener.onSuccess(rowIdx);
                } else {
                    resultListener.onFailure();
                }
            }
        } catch (Exception e) {
            resultListener.onFailure();
        }
    }

    public void insertAnswer(AnswerModel answerModel, DatabaseResultListener<Long> resultListener) {
        try {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(AnswerTableColumns.KeyQuestionId.key, answerModel.getQuestionId());
            contentValues.put(AnswerTableColumns.KeyVideo.key, answerModel.getVideo());
            contentValues.put(AnswerTableColumns.KeyCreatedDate.key, answerModel.getCreatedDate());

            if(database != null) {
                Long rowIdx = database.insert(ANSWER_TABLE_NAME, null, contentValues);
                if (rowIdx != -1) {
                    resultListener.onSuccess(rowIdx);
                } else {
                    resultListener.onFailure();
                }
            }
        } catch (Exception e) {
            resultListener.onFailure();
        }
    }

    public List<QuestionModel> getAllQuestions() {
        List<QuestionModel> questions = new ArrayList<>();
        Arrays.stream(QuestionTableColumns.values()).map(it -> it.key);
        String[] projection = { QuestionTableColumns.KeyId.key,
                QuestionTableColumns.KeyContent.key,
                QuestionTableColumns.KeyExpiredTime.key,
                QuestionTableColumns.KeyCompany.key,
                QuestionTableColumns.KeyQuestionType.key
        };
        Cursor cursor = database.query(
                QUESTION_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor != null) {
            while (cursor.moveToNext()) {
                QuestionModel question = new QuestionModel();
                question.setId(cursor.getInt(0));
                question.setContent(cursor.getString(1));
                question.setExpiredTime(cursor.getInt(2));
                question.setCompany(cursor.getString(3));
                question.setType(cursor.getString(4));
                questions.add(question);
            }
        }
        return questions;
    }

    public List<AnswerWithQuestionEntity> getAnswers(Integer id) {
        Cursor cursor;
        List<AnswerWithQuestionEntity> answerWithQuestionList = new ArrayList<>();
        Arrays.stream(AnswerTableColumns.values()).map(it -> it.key);

        if(id == null) {
            cursor = database.rawQuery(
                    "SELECT * FROM " +
                            ANSWER_TABLE_NAME +
                            " LEFT JOIN " +
                            QUESTION_TABLE_NAME + " ON " +
                            ANSWER_TABLE_NAME + "." + AnswerTableColumns.KeyQuestionId.key + " = " +
                            QUESTION_TABLE_NAME + ".id",
                    null
            );
        } else {
            cursor = database.rawQuery(
                    "SELECT * FROM " +
                            ANSWER_TABLE_NAME +
                            " LEFT JOIN " +
                            QUESTION_TABLE_NAME + " ON " +
                            ANSWER_TABLE_NAME + "." + AnswerTableColumns.KeyQuestionId.key + " = " +
                            QUESTION_TABLE_NAME + ".id" +
                            " WHERE " + ANSWER_TABLE_NAME + "." + AnswerTableColumns.KeyQuestionId.key + " = " + id,
                    null
            );
        }

        try {
            if(cursor != null) {
                while (cursor.moveToNext()) {
                    AnswerWithQuestionEntity answerWithQuestion= new AnswerWithQuestionEntity();
                    AnswerModel answerModel = new AnswerModel();
                    QuestionModel questionModel = new QuestionModel();

                    answerModel.setId(cursor.getInt(0));
                    answerModel.setQuestionId(cursor.getInt(1));
                    answerModel.setVideo(cursor.getString(2));
                    answerModel.setCreatedDate(cursor.getString(3));

                    questionModel.setId(cursor.getInt(4));
                    questionModel.setContent(cursor.getString(5));
                    questionModel.setExpiredTime(cursor.getInt(6));
                    questionModel.setCompany(cursor.getString(7));
                    questionModel.setType(cursor.getString(8));

                    answerWithQuestion.setAnswer(answerModel);
                    answerWithQuestion.setQuestion(questionModel);

                    answerWithQuestionList.add(answerWithQuestion);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return answerWithQuestionList;
    }

    public Boolean deleteAnswer(Integer answerId) {
        try {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            return database.delete(ANSWER_TABLE_NAME, AnswerTableColumns.KeyId.key + "=" + answerId, null) > 0;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean deleteQuestion(Integer questionId) {
        try {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            return database.delete(QUESTION_TABLE_NAME, QuestionTableColumns.KeyId.key + "=" + questionId, null) > 0;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean updateQuestion(QuestionModel questionModel) {
        String query = "UPDATE " + QUESTION_TABLE_NAME + " SET " +
                QuestionTableColumns.KeyContent.key + " = " + "'" + questionModel.getContent() + "'" + "," +
                QuestionTableColumns.KeyExpiredTime.key + " = " + questionModel.getExpiredTime() + "," +
                QuestionTableColumns.KeyCompany.key + " = " + "'" + questionModel.getCompany() + "'" + "," +
                QuestionTableColumns.KeyQuestionType.key + " = " + "'" + questionModel.getType() + "'" +
                " WHERE " + QuestionTableColumns.KeyId.key + " = " + questionModel.getId();

        try {
            database.execSQL(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
