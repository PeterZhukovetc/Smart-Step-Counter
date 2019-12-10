package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.project.smart_step_counter.data.DatabaseContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private Button toMainScreenBtn;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private TextView generalStepsTxt;
    private TextView generalDistanceTxt;

    private GraphView graph;
    private LineGraphSeries<DataPoint> series;
    //private SimpleDateFormat;=new SimpleDateFormat("M");

    private StatisticsUpdateThread statisticsUpdateThread;

    private Handler mainHandler=new Handler();

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Log.d(LOG_TAG, "StatisticsActivity created");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        generalDistanceTxt=findViewById(R.id.generalDistanceTxt);
        generalStepsTxt=findViewById(R.id.generalStepsTxt);

        graph=findViewById(R.id.statisticsGraph);


        statisticsUpdateThread=new StatisticsUpdateThread();
        databaseHelper=new DatabaseHelper(this);



        toMainScreenBtn= findViewById(R.id.toMainScreenFromStatisticsBtn);
        toMainScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, MainScreenActivity.class));
            }
        });
    }

//    private DataPoint[] getDataPoint(Cursor cursor){
//        database=databaseHelper.getReadableDatabase();
//
//        ArrayList<DataPoint> dataPoints=new ArrayList<>();
//        do{
//            int stepsIndex=cursor.getColumnIndex(DatabaseContract.Statistics.COLUMN_STEPS);
//            int monthIndex=cursor.getColumnIndex(DatabaseContract.Statistics.COLUMN_MONTH);
//            int dayIndex=cursor.getColumnIndex(DatabaseContract.Statistics.COLUMN_DAY);
//
//            int steps=cursor.getInt(stepsIndex);
//            int month=cursor.getInt(monthIndex);
//            int day=cursor.getInt(dayIndex);
//
//
//
//        }while(cursor.moveToNext());
//
//
//    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "StatisticsActivity Destroyed");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "StatisticsActivity paused");
        super.onPause();

    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "StatisticsActivity resumed");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "StatisticsActivity stopped");
        super.onStop();
        statisticsUpdateThread.interrupt();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "StatisticsActivity started");
        super.onStart();
        statisticsUpdateThread.start();
    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "StatisticsActivity restarted");
        super.onRestart();
    }

    private class StatisticsUpdateThread extends Thread{
        public void run(){
            database = databaseHelper.getReadableDatabase();
            String[] projectionGeneral= {
                    DatabaseContract.General.COLUMN_STEPS,
                    DatabaseContract.General.COLUMN_DISTANCE,
            };
            String[] projectionStatistics={
                    DatabaseContract.Statistics._ID,
                    DatabaseContract.Statistics.COLUMN_STEPS

            };

//            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
//                    @Override
//                    public String formatLabel(double value, boolean isValueX) {
//
//                    }
//
//                    });

            try{
                while (true) {
                    Cursor cursor1 = database.query(
                            DatabaseContract.General.TABLE_NAME,
                            projectionGeneral,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    int stepsIndex = cursor1.getColumnIndex(DatabaseContract.General.COLUMN_STEPS);
                    int distanceIndex=cursor1.getColumnIndex(DatabaseContract.General.COLUMN_DISTANCE);

                    cursor1.moveToFirst();

                    Integer steps = cursor1.getInt(stepsIndex);
                    Integer distance=cursor1.getInt(distanceIndex);

                    cursor1.close();


                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            generalDistanceTxt.setText(distance.toString());
                        }
                    });
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            generalStepsTxt.setText(steps.toString());
                        }
                    });


//                    Cursor cursor2 = database.query(
//                            DatabaseContract.Statistics.TABLE_NAME,
//                            projectionStatistics,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null
//                    );
//
//                    if(cursor2.getCount()==0){
//                        cursor2.close();
//                    }
//                    else {
//                        series=new LineGraphSeries<>(getDataPoint(cursor2));
//
//
//
//
//
//                        cursor2.close();
//                    }



//                    int statisticsStepsIndex = cursor.getColumnIndex(DatabaseContract.Statistics.COLUMN_STEPS);
//
//
//
//                    ////////////////////
//
//
//
//
//                    Integer steps = cursor.getInt(stepsIndex);
//                    Integer distance=cursor.getInt(distanceIndex);
//                    cursor.close();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            generalDistanceTxt.setText(distance.toString());
                        }
                    });
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            generalStepsTxt.setText(steps.toString());
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
