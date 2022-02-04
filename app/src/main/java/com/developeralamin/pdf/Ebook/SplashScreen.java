package com.developeralamin.pdf.Ebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.developeralamin.pdf.R;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Animation topAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.to_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView.setAnimation(topAnimation);
        textView.setAnimation(bottomAnimation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, EbookActivity.class));
                finish();
            }
        },3000);
    }
}