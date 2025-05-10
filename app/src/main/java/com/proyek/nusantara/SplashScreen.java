package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    TextView tvSplashScreen;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        session = new SessionManager(this);

        tvSplashScreen = findViewById(R.id.tvSplashScreen);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_anim);
        tvSplashScreen.setAnimation(animation);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(decorView.SYSTEM_UI_FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            Intent intent;
            if (session.isLoggedIn()) {
                // Sudah login → langsung MainActivity
                intent = new Intent(SplashScreen.this, MainActivity.class);
            } else {
                // Belum login → ke LoginActivity
                intent = new Intent(SplashScreen.this, LoginActivity.class);
            }
            // Bersihkan back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 3000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}