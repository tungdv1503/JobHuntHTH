package com.jobhunthth.HTH0205.Register_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jobhunthth.HTH0205.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Find the logo ImageView and loading ProgressBar
        ImageView logoImageView = findViewById(R.id.logoImageView);
        ProgressBar loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Zoom in the logo for 3 seconds
        ObjectAnimator animator = ObjectAnimator.ofFloat(logoImageView, "scaleX", 1.0f, 1.2f);
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(logoImageView, "scaleY", 1.0f, 1.2f);
        animator2.setDuration(3000);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();

        // Move up the logo a little bit after the zoom animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(logoImageView, "translationY", -50f);
                animator.setDuration(500);
                animator.start();
            }
        }, 3000);

        // Simulate some loading process with a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the logo and show the loading animation
                logoImageView.animate().alpha(0).setDuration(300);
                loadingProgressBar.setVisibility(View.VISIBLE);

                // Start your main activity here
                Intent intent = new Intent(Start.this, Login.class);
                startActivity(intent);

                // Close this activity
                finish();
            }
        },0);
    }
}