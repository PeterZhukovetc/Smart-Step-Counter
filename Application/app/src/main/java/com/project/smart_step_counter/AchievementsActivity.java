package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AchievementsActivity extends AppCompatActivity {

    private Button toMainScreenBtn;
    private Button addGoalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        toMainScreenBtn=(Button) findViewById(R.id.toMainScreenFromAchievementsBtn);
        toMainScreenBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AchievementsActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });
        addGoalBtn=(Button) findViewById(R.id.addGoalBtn);
        addGoalBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AchievementsActivity.this, CreateGoalActivity.class);
                startActivity(intent);
            }
        });
    }
}
