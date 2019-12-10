package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.smart_step_counter.data.DatabaseContract;

public class MainScreenActivity extends AppCompatActivity {

    private ImageButton settingsButton;
    private Button toAchievementsBtn;
    private Button toStatisticsBtn;
    private Button countingBtn;
    private TextView stepsValueTxt;
    private TextView distanceValueTxt;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private StepsShowThread stepsShowThread;

    private Handler mainHandler=new Handler();

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Log.d(LOG_TAG, "MainScreenActivity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        stepsValueTxt=findViewById(R.id.stepsCountTxt);
        distanceValueTxt=findViewById(R.id.distanceCountTxt);

        databaseHelper=new DatabaseHelper(this);

        stepsShowThread=new StepsShowThread();


        countingBtn=findViewById(R.id.countingBtn);
        settingsButton = findViewById(R.id.settingsButton);
        toAchievementsBtn =  findViewById(R.id.toAchievementsBtn);
        toStatisticsBtn =  findViewById(R.id.toStatisticsBtn);



        //!!!!!!!!!!!!!!!!
        countingBtn.setOnClickListener(v->{
            System.out.println(countingBtn.getText()+"     "+countingBtn.getText().toString());
            if(countingBtn.getText().toString().equals("Пауза")){
                countingBtn.setText("Пуск");
                stopService(new Intent(MainScreenActivity.this,ApplicationService.class));
            }
            else if(countingBtn.getText().toString().equals("Пуск")){
                countingBtn.setText("Пауза");
                startService(new Intent(MainScreenActivity.this,ApplicationService.class));
            }
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


        toAchievementsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, AchievementsActivity.class);
            startActivity(intent);
        });

        toStatisticsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreenActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "MainScreenActivity Destroyed");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "MainScreenActivity paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "MainScreenActivity resumed");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "MainScreenActivity stopped");
        super.onStop();
        stepsShowThread.interrupt();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "MainScreenActivity started");
        super.onStart();
        //startService(new Intent(MainScreenActivity.this,ApplicationService.class));
        if (stepsShowThread.getState() == Thread.State.NEW)
        {
            stepsShowThread.start();
        }

    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "MainScreenActivity restarted");
        super.onRestart();
    }


    private class StepsShowThread extends Thread{
        public void run(){
            database = databaseHelper.getReadableDatabase();
            String[] projection = {
                    DatabaseContract.Profile._ID,
                    DatabaseContract.Profile.COLUMN_HEIGHT,
                    DatabaseContract.Profile.COLUMN_STEPS,
                    DatabaseContract.Profile.COLUMN_ISFIRST
            };
            try{
                while (true) {
                    Cursor cursor = database.query(
                            DatabaseContract.Profile.TABLE_NAME,
                            projection,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    int stepsIndex = cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_STEPS);
                    int heightIndex=cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_HEIGHT);
                    cursor.moveToFirst();
                    Integer steps = cursor.getInt(stepsIndex);
                    Integer height=cursor.getInt(heightIndex);
                    cursor.close();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            stepsValueTxt.setText(steps.toString());
                        }
                    });
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            distanceValueTxt.setText(new Float((((height)/4+37)*0.01)*steps).toString());
                        }
                    });
                    Thread.sleep(1500);

                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
