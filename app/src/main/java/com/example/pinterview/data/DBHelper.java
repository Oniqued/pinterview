package com.example.pinterview.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String QUESTION_TABLE_NAME = "questionTable";
    public static final String ANSWER_TABLE_NAME = "answerTable";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String questionSql = "CREATE TABLE if not exists " +  QUESTION_TABLE_NAME + "("
                + QuestionTableColumns.KeyId.key + " integer primary key autoincrement,"
                + QuestionTableColumns.KeyContent.key + " text, "
                + QuestionTableColumns.KeyExpiredTime.key + " integer, "
                + QuestionTableColumns.KeyCompany.key + " text, "
                + QuestionTableColumns.KeyQuestionType.key + " text);";

        String answerSql = "CREATE TABLE if not exists " + ANSWER_TABLE_NAME + "("
                + AnswerTableColumns.KeyId.key + " integer primary key autoincrement,"
                + AnswerTableColumns.KeyQuestionId.key + " integer, "
                + AnswerTableColumns.KeyVideo.key + " text, "
                + AnswerTableColumns.KeyCreatedDate.key + " text);";

        db.execSQL(questionSql);
        db.execSQL(answerSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String questionSql = "DROP TABLE if exists " + QUESTION_TABLE_NAME;
        String answerSql = "DROP TABLE if exists " + ANSWER_TABLE_NAME;

        db.execSQL(questionSql);
        db.execSQL(answerSql);
        onCreate(db);
    }
}