package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.utils.StatusBar;

public class LaunchActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        StatusBar.fitSystemBar(this);
//        StatusBar.lightStatusBar(this, true);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}