package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ConfirmingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirming);
        setFinishOnTouchOutside(false);
    }
}
