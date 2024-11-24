package com.example.boost.main.timer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.boost.R;

public class TimerFragment extends Fragment {

    private int SetHour;
    private int SetMinutes;
    private int SetSecond;
    private int timeInSeconds;
    private ProgressBar progressBarCircle;
    private ImageButton plybtn;
    private ImageButton addtime;
    private ImageButton resetStopBtn;
    private Button edit;
    private TextView textViewTime;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long remainingTimeInMillis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_timer, container, false);

        edit = view.findViewById(R.id.editbtn);
        addtime = view.findViewById(R.id.adt);
        progressBarCircle = view.findViewById(R.id.timeprogress);
        textViewTime = view.findViewById(R.id.time);
        plybtn = view.findViewById(R.id.ply);
        resetStopBtn = view.findViewById(R.id.rtnst);

        resetStopBtn.setVisibility(View.GONE);
        updateUI();

        edit.setOnClickListener(v -> showDialog());
        addtime.setOnClickListener(v -> showDialog());

        plybtn.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        resetStopBtn.setOnClickListener(v -> resetTimer());

        getChildFragmentManager().setFragmentResultListener("setTimeKey", this, (requestKey, result) -> {
            int hours = result.getInt("hours");
            int minutes = result.getInt("minutes");
            int seconds = result.getInt("seconds");

            onTimeSet(hours, minutes, seconds);
        });

        return view;
    }

    private void onTimeSet(int hours, int minutes, int seconds) {
        SetHour = hours;
        SetMinutes = minutes;
        SetSecond = seconds;

        timeInSeconds = (SetHour * 3600) + (SetMinutes * 60) + SetSecond;
        updateUI();
    }

    private void updateUI() {
        textViewTime.setText(formatTime(timeInSeconds * 1000L));
        progressBarCircle.setProgress(0);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        remainingTimeInMillis = 0;
        isTimerRunning = false;
        updateUI();
        plybtn.setImageResource(R.drawable.ply);
        resetStopBtn.setVisibility(View.GONE);
    }

    private void startTimer() {
        long totalDuration = (remainingTimeInMillis > 0) ? remainingTimeInMillis : timeInSeconds * 1000L;

        countDownTimer = new CountDownTimer(totalDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInMillis = millisUntilFinished;
                textViewTime.setText(formatTime(millisUntilFinished));
                float progress = (float) ((millisUntilFinished * 100) / (timeInSeconds * 1000L));
                progressBarCircle.setProgress((int) progress);
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                plybtn.setImageResource(R.drawable.ply);
                resetStopBtn.setVisibility(View.GONE);
                textViewTime.setText(formatTime(0));
            }
        };

        isTimerRunning = true;
        plybtn.setImageResource(R.drawable.pause);
        resetStopBtn.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        plybtn.setImageResource(R.drawable.ply);
    }

    private String formatTime(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void showDialog() {
        dialogset dialogFragment = new dialogset();
        dialogFragment.show(getChildFragmentManager(), "SetTimeDialog");
    }
}
