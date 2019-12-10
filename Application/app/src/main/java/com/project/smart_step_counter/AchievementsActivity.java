package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.smart_step_counter.data.DatabaseContract;

public class AchievementsActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    private Button toMainScreenBtn;
    private Button addGoalBtn;

    private TextView[] goalTxt;
    private TextView[] achievementsTxt;
    private TextView recordStepsTxt;
    private TextView recordDistanceTxt;


    //private String text;
    private int counter;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private AchievementsUpdateThread achievementsUpdateThread;

    private Handler mainHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "AchievementsActivity created");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        goalTxt=new TextView[2];
        achievementsTxt=new TextView[2];

        goalTxt[0]=findViewById(R.id.goal1Txt);
        goalTxt[1]=findViewById(R.id.goal2Txt);

        achievementsTxt[0]=findViewById(R.id.achievement1Txt);
        achievementsTxt[1]=findViewById(R.id.achievement2Txt);

        recordStepsTxt=findViewById(R.id.recordStepsTxt);
        recordDistanceTxt=findViewById(R.id.recordDistanceTxt);

        achievementsUpdateThread=new AchievementsUpdateThread();
        databaseHelper=new DatabaseHelper(this);


        toMainScreenBtn=findViewById(R.id.toMainScreenFromAchievementsBtn);

        toMainScreenBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AchievementsActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });

        addGoalBtn= findViewById(R.id.addGoalBtn);

        addGoalBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AchievementsActivity.this, CreateGoalActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "AchievementsActivity Destroyed");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "AchievementsActivity paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "AchievementsActivity resumed");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "AchievementsActivity stopped");
        super.onStop();
        achievementsUpdateThread.interrupt();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "AchievementsActivity started");
        super.onStart();
//        achievementsUpdateThread.start();
        if (achievementsUpdateThread.getState() == Thread.State.NEW)
        {
            achievementsUpdateThread.start();
        }
    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "AchievementsActivity restarted");
        super.onRestart();
    }

    private class AchievementsUpdateThread extends Thread{
        public void run(){

            String[] projectionGoals= {
                    DatabaseContract.Goals.COLUMN_NAME,
                    DatabaseContract.Goals.COLUMN_STEPS,
                    DatabaseContract.Goals.COLUMN_DISTANCE,
                    DatabaseContract.Goals.COLUMN_DONE,
                    DatabaseContract.Goals.COLUMN_TYPE
            };
            String[] projectionAchievements={
                    DatabaseContract.Achievements.COLUMN_NAME,
                    DatabaseContract.Achievements.COLUMN_STEPS,
                    DatabaseContract.Achievements.COLUMN_DISTANCE,
                    DatabaseContract.Achievements.COLUMN_DONE,
                    DatabaseContract.Achievements.COLUMN_TYPE
            };

            String[] projectionRecords={
                    DatabaseContract.Records.COLUMN_STEPS,
                    DatabaseContract.Records.COLUMN_DISTANCE
            };

            try{
                while (true) {
                    database = databaseHelper.getReadableDatabase();
                    Cursor cursor1 = database.query(
                            DatabaseContract.Goals.TABLE_NAME,
                            projectionGoals,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    int goalNameIndex = cursor1.getColumnIndex(DatabaseContract.Goals.COLUMN_NAME);
                    int goalsDoneIndex=cursor1.getColumnIndex(DatabaseContract.Goals.COLUMN_DONE);
                    int goalsDistanceIndex=cursor1.getColumnIndex(DatabaseContract.Goals.COLUMN_DISTANCE);
                    int goalsStepsIndex=cursor1.getColumnIndex(DatabaseContract.Goals.COLUMN_STEPS);
                    int goalsTypeIndex=cursor1.getColumnIndex(DatabaseContract.Goals.COLUMN_TYPE);

                    cursor1.moveToFirst();
                    counter=0;
                    if(cursor1.getCount()!=0){
                        do{
                            String goalName = cursor1.getString(goalNameIndex);
                            Integer goalsDone = cursor1.getInt(goalsDoneIndex);
                            Integer goalsDistance = cursor1.getInt(goalsDistanceIndex);
                            Integer goalsSteps = cursor1.getInt(goalsStepsIndex);
                            Integer goalsType = cursor1.getInt(goalsTypeIndex);

                            if(goalsType==0){
                                goalName=goalName.concat(" "+goalsSteps.toString()+ " шага(-ов)");
                            }
                            else if(goalsType==1){
                                goalName=goalName.concat(" "+goalsDistance.toString()+ " м");
                            }
                            final String text=goalName;
                            final int count=counter;

                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    goalTxt[count].setText(text);
                                    if (goalsDone == 1)
                                        goalTxt[count].setBackgroundColor(Color.GREEN);
                                }
                            });
                            ++counter;
                            Thread.sleep(100);
                        }while(cursor1.moveToNext());
                    }

                    cursor1.close();


                    Cursor cursor2 = database.query(
                            DatabaseContract.Achievements.TABLE_NAME,
                            projectionAchievements,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    int achievementsNameIndex = cursor2.getColumnIndex(DatabaseContract.Achievements.COLUMN_NAME);
                    int achievementsDoneIndex=cursor2.getColumnIndex(DatabaseContract.Achievements.COLUMN_DONE);
                    int achievementsDistanceIndex=cursor2.getColumnIndex(DatabaseContract.Achievements.COLUMN_DISTANCE);
                    int achievementsStepsIndex=cursor2.getColumnIndex(DatabaseContract.Achievements.COLUMN_STEPS);
                    int achievementsTypeIndex=cursor2.getColumnIndex(DatabaseContract.Achievements.COLUMN_TYPE);

                    cursor2.moveToFirst();

                    counter=0;
                    do{
                        String achievementsName = cursor2.getString(achievementsNameIndex);
                        Integer achievementsDone= cursor2.getInt(achievementsDoneIndex);
                        Integer achievementsDistance = cursor2.getInt(achievementsDistanceIndex);
                        Integer achievementsSteps = cursor2.getInt(achievementsStepsIndex);
                        Integer achievementsType = cursor2.getInt(achievementsTypeIndex);

                        if(achievementsType==0){
                            achievementsName=achievementsName.concat(" "+achievementsSteps.toString()+ " шага(-ов)");
                        }
                        else if(achievementsType==1){
                            achievementsName=achievementsName.concat(" "+achievementsDistance.toString()+ " м");
                        }
                        final String text=achievementsName;
                        final int count=counter;
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                achievementsTxt[count].setText(text);
                                if (achievementsDone == 1)
                                    achievementsTxt[count].setBackgroundColor(Color.GREEN);
                            }
                        });
                        Thread.sleep(100);
                        ++counter;
                    }while(cursor2.moveToNext());

                    counter=0;

                    cursor2.close();

                    Cursor cursor3 = database.query(
                            DatabaseContract.Records.TABLE_NAME,
                            projectionRecords,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    cursor3.moveToFirst();

                    int recordsStepsIndex = cursor3.getColumnIndex(DatabaseContract.Records.COLUMN_STEPS);
                    int recordsDistanceIndex=cursor3.getColumnIndex(DatabaseContract.Records.COLUMN_DISTANCE);

                    Integer recordsSteps = cursor3.getInt(recordsStepsIndex);
                    Integer recordsDistance = cursor3.getInt(recordsDistanceIndex);

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            recordStepsTxt.setText(recordsSteps.toString());
                            recordDistanceTxt.setText(recordsDistance.toString());
                        }
                    });

                    cursor3.close();
                    Thread.sleep(400);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
