package com.mrizki.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txtTimerDefault;
    private TextView txtTime;
    private SeekBar skBrTimer;
    private Button btnControl;
    private Button btnStopRinging;
    private CountDownTimer countDownTimer;
    private MediaPlayer mPlayer;

    private boolean isCounting = false;

    public int max = 600;
    public int defaultTime = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        skBrTimer = findViewById(R.id.skBrTimer);
        txtTimerDefault = findViewById(R.id.txtTimerDefault);
        txtTime = findViewById(R.id.txtTime);
        btnControl = findViewById(R.id.btnControl);
        btnStopRinging = findViewById(R.id.btnStopRinging);

        btnStopRinging.setVisibility(View.INVISIBLE);

        skBrTimer.setMax(max);

        skBrTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String timerString = updateTimer(progress);

                txtTimerDefault.setText("Timer is set to: " + timerString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skBrTimer.setProgress(defaultTime);
    }

    public void toggleTimer(View view) {
        if (isCounting) {
            finishCounting();
        } else {
            skBrTimer.setEnabled(false);
            btnControl.setText("STOP!");

            countDownTimer = new CountDownTimer(skBrTimer.getProgress() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mechanical_clock_ring);
                    mPlayer.start();
                    btnStopRinging.setVisibility(View.VISIBLE);
                    finishCounting();
                }
            }.start();

            isCounting = true;
        }
    }

    public void stopAlarm(View view) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        btnStopRinging.setVisibility(View.INVISIBLE);
    }

    public String updateTimer(int secondsLeft) {
        // Convert seconds to minutes
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String minutesString;
        String secondsString;
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = minutes + "";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = seconds + "";
        }

        txtTime.setText(minutesString + ":" + secondsString);
        return minutesString + ":" + secondsString;
    }

    private void finishCounting() {
        skBrTimer.setProgress(defaultTime);
        skBrTimer.setEnabled(true);

        countDownTimer.cancel();
        btnControl.setText("Go!");
        isCounting = false;
    }
}
