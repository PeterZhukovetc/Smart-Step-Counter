package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button exitSettingsBtn;
    private Button deleteProfileBtn;
    private Button changeHeightBtn;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Log.d(LOG_TAG, "SettingsActivity created");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        exitSettingsBtn= findViewById(R.id.exitSettingsBtn);

        exitSettingsBtn.setOnClickListener(v -> {
            Intent intent=new Intent(SettingsActivity.this, MainScreenActivity.class);
            startActivity(intent);
        });

        deleteProfileBtn= findViewById(R.id.deleteProfileBtn);

        deleteProfileBtn.setOnClickListener(v -> {
            Intent intent=new Intent(SettingsActivity.this, ConfirmingActivity.class);
            startActivity(intent);
        });

        changeHeightBtn= findViewById(R.id.changeHeightBtn);

        changeHeightBtn.setOnClickListener(v -> {
            Intent intent=new Intent(SettingsActivity.this, ChangeHeightActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "SettingsActivity Destroyed");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "SettingsActivity paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "SettingsActivity resumed");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "SettingsActivity stopped");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "SettingsActivity started");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "SettingsActivity restarted");
        super.onRestart();
    }
}
