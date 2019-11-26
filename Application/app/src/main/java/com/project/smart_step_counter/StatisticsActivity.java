package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StatisticsActivity extends AppCompatActivity {

    private Button toMainScreenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        toMainScreenBtn=(Button) findViewById(R.id.toMainScreenFromStatisticsBtn);
        toMainScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StatisticsActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}
