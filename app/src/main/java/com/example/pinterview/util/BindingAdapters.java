package com.example.pinterview.util;

import static com.example.pinterview.util.FileUtils.fileDateFormat;

import android.util.Log;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BindingAdapters {
    @BindingAdapter("setPracticeDate")
    public static void setPracticeDate(TextView view, String createdDate) {
        try {
            SimpleDateFormat koreanDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREAN);
            Log.e("date", createdDate);

            view.setText(
                    "(" + koreanDateFormat.format(fileDateFormat.parse(createdDate))  + " 연습 기록" + ")"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}