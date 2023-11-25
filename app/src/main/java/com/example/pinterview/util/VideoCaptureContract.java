package com.example.pinterview.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

public class VideoCaptureContract extends ActivityResultContracts.CaptureVideo {
    private Integer expireTime;

    public VideoCaptureContract(Integer expireTime) {
        this.expireTime = expireTime;
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull Uri input) {
        Intent intent = super.createIntent(context, input);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, expireTime);
        return intent;
    }
}
