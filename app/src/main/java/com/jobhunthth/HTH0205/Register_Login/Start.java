package com.jobhunthth.HTH0205.Register_Login;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jobhunthth.HTH0205.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Start extends AppCompatActivity {

    ImageView logo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Find the logo ImageView and loading ProgressBar
        logo = findViewById(R.id.logoImageView);
        progressBar = findViewById(R.id.loadingProgressBar);

        // Zoom in the logo for 3 seconds
        ObjectAnimator ani1 = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        ObjectAnimator aniX = ObjectAnimator.ofFloat(logo, "scaleX", 0f, 2.5f);
        ObjectAnimator aniY = ObjectAnimator.ofFloat(logo, "scaleY", 0f, 2.5f);
        ani1.setDuration(1500);
        aniX.setDuration(1500);
        aniY.setDuration(1500);

        AnimatorSet set = new AnimatorSet();
        set.play(ani1).with(aniX).with(aniY);
        set.start();
        aniprogress();
    }

        public void aniprogress() {
//        if(android.os.Build.VERSION.SDK_INT >= 11){
            // will update the "progress" propriety of seekbar until it reaches progress
            doStartProgressBar();
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 30000);
            animation.setDuration(3000); // 0.5 second
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(Start.this, Register.class));
                    finish();
                }
            }.start();
//        }
        }

    private void doStartProgressBar() {
        final int MAX = 100;
        this.progressBar.setMax(MAX);
        int progressIncrement = (int) ((2000 / (float) MAX) + 0.5);

        for (int i = 0; i < MAX; i++) {
            final int progress = i + 1;
            SystemClock.sleep(progressIncrement);

            progressBar.setProgress(progress);
            if (progress == MAX) {
                Toast.makeText(Start.this, "Chào Mừng", Toast.LENGTH_SHORT).show();
            }
        }
    }
}