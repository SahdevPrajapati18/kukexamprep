package com.kuk.kukexamprep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Kukexamprep);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // User is already logged in
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                // User not logged in
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish(); // close SplashActivity
        }, SPLASH_DELAY);
    }
}
