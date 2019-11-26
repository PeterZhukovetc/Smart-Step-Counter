package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainScreenActivity extends AppCompatActivity {

    private ImageButton settingsButton;
    private Button toAchievementsBtn;
    private Button toStatisticsBtn;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Integer count = 0;
    private TextView steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        toAchievementsBtn = (Button) findViewById(R.id.toAchievementsBtn);
        toAchievementsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, AchievementsActivity.class);
            startActivity(intent);
        });
        toStatisticsBtn = (Button) findViewById(R.id.toStatisticsBtn);
        toStatisticsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        final SensorEventListener mListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                count += event.values.length;
                steps.setText(count.toString());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(mListener, sensor, 100);
        steps = (TextView) findViewById(R.id.stepsCountTxt);


    }
}
