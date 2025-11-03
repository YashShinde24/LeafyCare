package com.example.leafy;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {

    private final String fullText = "LeafyCare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorPrimary = ContextCompat.getColor(this, R.color.primary);
        window.setNavigationBarColor(colorPrimary);

        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView signInText = findViewById(R.id.signintext);

        signInText.setText(Html.fromHtml(getString(R.string.signin_text)));
        signInText.setMovementMethod(LinkMovementMethod.getInstance());

        signInText.setOnClickListener(v -> {
            Intent intent = new Intent(SplashScreen.this, SignIn.class);
            startActivity(intent);
        });


        View circle = findViewById(R.id.circle);
        TextView leafyCareText = findViewById(R.id.appname);
        Button getStartedButton = findViewById(R.id.getstartedbutton);

        leafyCareText.setText("");
        leafyCareText.setVisibility(View.VISIBLE);

        float startX = 0f;
        float endX = 420f;
        int duration = 2000;

        ValueAnimator animator = ValueAnimator.ofFloat(startX, endX);
        animator.setDuration(duration);

        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            circle.setTranslationX(value);

            float progress = value / endX;
            int charCount = (int) (progress * fullText.length());
            charCount = Math.min(charCount, fullText.length());

            leafyCareText.setText(fullText.substring(0, charCount));
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Stop pulsing animation when circle reaches end
                circle.clearAnimation();

                // Start calm animation for circle (just stop pulsing, it stays at the end)
                circle.setTranslationX(endX);  // Keep the circle at the end

                // Add animation for "Get Started" button
                animateGetStartedButton(getStartedButton);
            }
        });

        animator.start();

        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(SplashScreen.this, QuestionOne.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }

    private void startPulsingAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f, 1f);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setDuration(700);
        scaleY.setDuration(700);

        scaleX.start();
        scaleY.start();
    }

    private void animateGetStartedButton(Button button) {
        // Use a Handler to apply scaling effect in intervals
        Handler handler = new Handler();
        Runnable scaleRunnable = new Runnable() {
            @Override
            public void run() {
                // Apply scaling effect to the "Get Started" button
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.1f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.1f, 1f);

                scaleX.setDuration(500);
                scaleY.setDuration(500);

                scaleX.start();
                scaleY.start();

                // Repeat the animation after a delay (e.g., 1000ms)
                handler.postDelayed(this, 1500);  // Delay between scaling
            }
        };

        // Start the interval-based scaling effect
        handler.postDelayed(scaleRunnable, 1500); // Initial delay before the first scaling effect
    }
}
