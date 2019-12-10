package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.project.smart_step_counter.data.DatabaseContract;

public class ProfileCreatingActivity extends AppCompatActivity  {
    final String LOG_TAG = "myLogs";

    private NumberPicker numberPicker;
    private TextView textView;
    private Button createProfileBtn;
    private Button countingBtn;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "ProfileCreatingActivity created");

        setContentView(R.layout.activity_main);
        textView= findViewById(R.id.textView2);
        numberPicker= findViewById(R.id.numPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);
        numberPicker.setValue(160);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        createProfileBtn =  findViewById(R.id.createProfileBtn);
        countingBtn=findViewById(R.id.countingBtn);

        databaseHelper=new DatabaseHelper(this);
        database=databaseHelper.getReadableDatabase();

        String[] projection={
                DatabaseContract.Profile._ID,
                DatabaseContract.Profile.COLUMN_HEIGHT,
                DatabaseContract.Profile.COLUMN_STEPS,
                DatabaseContract.Profile.COLUMN_ISFIRST
        };

        Cursor cursor=database.query(
                DatabaseContract.Profile.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int isFirstFlagIndex=cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_ISFIRST);
        cursor.moveToFirst();
        int isFirstFlagValue=cursor.getInt(isFirstFlagIndex);
        cursor.close();
        if(isFirstFlagValue==1){
            //database.close();
            startService(new Intent(ProfileCreatingActivity.this,ApplicationService.class));
            startActivity(new Intent(ProfileCreatingActivity.this, MainScreenActivity.class));
        }




        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsFirstFlag();
                setHeight(numberPicker.getValue());
                startService(new Intent(ProfileCreatingActivity.this, ApplicationService.class));
                Intent intent1=new Intent(ProfileCreatingActivity.this, MainScreenActivity.class);
                startActivity(intent1);
            }
        });




    }

    private void setIsFirstFlag(){
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Profile.COLUMN_ISFIRST,1);
        database.update(
                DatabaseContract.Profile.TABLE_NAME,
                values,
                DatabaseContract.Profile._ID+"=?",
                new String[]{Integer.toString(1)});
    }

    private void setHeight(int height){
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Profile.COLUMN_HEIGHT,height);
        database.update(
                DatabaseContract.Profile.TABLE_NAME,
                values,
                DatabaseContract.Profile._ID+"=?",
                new String[]{Integer.toString(1)});
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "ProfileCreatingActivity Destroyed");
        Intent intent=new Intent(ProfileCreatingActivity.this, ApplicationService.class);
        stopService(intent);
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "ProfileCreatingActivity paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "ProfileCreatingActivity resumed");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "ProfileCreatingActivity stopped");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "ProfileCreatingActivity started");
        super.onStart();
//        databaseHelper=new DatabaseHelper(this);
//        database=databaseHelper.getReadableDatabase();
//
//        String[] projection={
////                DatabaseContract.Profile._ID,
////                DatabaseContract.Profile.COLUMN_HEIGHT,
////                DatabaseContract.Profile.COLUMN_STEPS,
//                DatabaseContract.Profile.COLUMN_ISFIRST
//        };
//
//        Cursor cursor=database.query(
//                DatabaseContract.Profile.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        int isFirstFlagIndex=cursor.getColumnIndex(DatabaseContract.Profile.COLUMN_ISFIRST);
//        cursor.moveToFirst();
//        int isFirstFlagValue=cursor.getInt(isFirstFlagIndex);
//        cursor.close();
//        if(isFirstFlagValue==0){
//            setContentView(R.layout.activity_main);
//            numberPicker.setMinValue(0);
////            numberPicker.setMaxValue(250);
////            numberPicker.setValue(160);
//            numberPicker.setWrapSelectorWheel(true);
//            setIsFirstFlag();
//            database.close();
//            //service
//        }
//        else if(isFirstFlagValue==1){
//            database.close();
//            startActivity(new Intent(ProfileCreatingActivity.this, MainScreenActivity.class));
//        }



    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "ProfileCreatingActivity restarted");
        super.onRestart();
    }

    NumberPicker.OnValueChangeListener onValueChangeListener=
            new NumberPicker.OnValueChangeListener() {
                @SuppressLint("ShowToast")
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Toast.makeText(ProfileCreatingActivity.this,
                            "selected"+numberPicker.getValue(),Toast.LENGTH_SHORT);
                }
            };


}
