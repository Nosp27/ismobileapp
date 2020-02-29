package com.pashikhmin.ismobileapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {
    private static final int AUTO_HIDE_DELAY_MILLIS = 1700;
    private final Handler mHideHandler = new Handler();
    private Class<? extends Activity> nextActivityClass = MenuActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
    }
    
    private void delayedHide(int delayMillis) {
        mHideHandler.postDelayed(this::callNextActivity, delayMillis);
    }

    private void callNextActivity() {
        Intent intent = new Intent(this, nextActivityClass);
        startActivity(intent);
        finish();
    }
}
