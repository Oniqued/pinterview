package com.example.pinterview.practice;

import static com.example.pinterview.questions.QuestionListActivity.APP_MODE;
import static com.example.pinterview.questions.QuestionListActivity.SELECTED_ITEM;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.pinterview.questions.DeleteDialogFragment;
import com.example.pinterview.util.FileUtils;
import com.example.pinterview.data.AnswerWithQuestionEntity;
import com.example.pinterview.data.PinterViewDataSource;
import com.example.pinterview.databinding.ActivityPracticeBinding;
import com.example.pinterview.util.VideoCaptureContract;
import com.google.gson.Gson;

import java.io.File;
import java.util.Map;

public class PracticeActivity extends AppCompatActivity {
    private ActivityPracticeBinding binding;
    private PracticeViewModel viewModel;
    private ActivityResultLauncher<Uri> recordVideoLauncher;
    private File file;
    private final String[] PERMISSIONS = {Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(PracticeViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        getQuestion();
        navBack();
        recordVideo();
        deleteVideo();
        saveAnswer();
        setVideoPlayer();
        deleteAnswer();
    }

    private ActivityResultLauncher<String[]> checkCameraPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            int denied = (int) result.values().stream().filter(it -> !it).count();
            Pair<File, Uri> fileInfo =  new FileUtils(PracticeActivity.this).createVideoFileUri();
            Uri uri = fileInfo.second;
            file = fileInfo.first;
            viewModel.changeVideoUri(uri);
            if(denied <= 0) { recordVideoLauncher.launch(uri); }
        }
    });

    public void initializeRecordVideoLauncher(Integer time) {
        recordVideoLauncher = registerForActivityResult(new VideoCaptureContract(time), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result) {

                } else {
                    binding.textviewNotice.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getQuestion() {
        String jsonQuestion = getIntent().getStringExtra(SELECTED_ITEM);
        AnswerWithQuestionEntity answerWithQuestionEntity = new Gson().fromJson(jsonQuestion, AnswerWithQuestionEntity.class);
        viewModel.changeQuestion(answerWithQuestionEntity);
        viewModel.changeAppMode(getIntent().getStringExtra(APP_MODE));
        initializeRecordVideoLauncher(answerWithQuestionEntity.getQuestion().getExpiredTime());
    }

    private void setVideoPlayer() {
        viewModel.video.observe(this, new Observer<Uri>() {
            @OptIn(markerClass = UnstableApi.class) @Override
            public void onChanged(Uri video) {
                if(video != null) {
                    ExoPlayer exoPlayer = new ExoPlayer.Builder(PracticeActivity.this).build();
                    MediaItem mediaItem = MediaItem.fromUri(video);
                    exoPlayer.setMediaItem(mediaItem);
                    exoPlayer.prepare();
                    binding.player.setPlayer(exoPlayer);
                    binding.textviewNotice.setVisibility(View.GONE);
                } else {
                    binding.textviewNotice.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void recordVideo() {
        binding.textviewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermissions.launch(PERMISSIONS);
            }
        });
    }

    private void deleteVideo() {
        binding.textviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.changeVideoUri(null);
            }
        });
    }

    private void saveAnswer() {
        binding.textviewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.saveVideo(viewModel.video.getValue(), new PinterViewDataSource.DatabaseResultListener<Long>() {
                    @Override
                    public void onSuccess(Long data) {
                        Toast.makeText(PracticeActivity.this, "면접 연습 기록이 저장되었어요.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(PracticeActivity.this, "영상이 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void deleteAnswer() {
        binding.textviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteDialogFragment().setDeleteCallback(new DeleteDialogFragment.DeleteCallback() {
                    @Override
                    public void delete() {
                        Boolean result = viewModel.deleteAnswer();
                        if(result) {
                            Toast.makeText(PracticeActivity.this, "연습 기록을 삭제했어요.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).show(getSupportFragmentManager(), "");
            }
        });
    }

    private void navBack() {
        binding.imageviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}