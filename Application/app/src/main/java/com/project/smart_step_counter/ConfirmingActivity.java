package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.smart_step_counter.data.DatabaseContract;

public class ConfirmingActivity extends AppCompatActivity {

    private Button confirmBtn;
    private Button cancelBtn;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirming);
        confirmBtn=findViewById(R.id.confirmProfileDeleteBtn);
        cancelBtn=findViewById(R.id.cancelProfileDeletingBtn);

        databaseHelper=new DatabaseHelper(this);





        cancelBtn.setOnClickListener(v -> {
            startActivity(new Intent(ConfirmingActivity.this, SettingsActivity.class));
        });
        confirmBtn.setOnClickListener(v -> {
            //resetIsFirstFlag();
            destroyDatabase();
            stopService(new Intent(ConfirmingActivity.this,ApplicationService.class));
            startActivity(new Intent(ConfirmingActivity.this, ProfileCreatingActivity.class));
        });
        setFinishOnTouchOutside(false);
    }

    private void resetIsFirstFlag(){
        database=databaseHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Profile.COLUMN_ISFIRST,0);
        database.update(
                DatabaseContract.Profile.TABLE_NAME,
                values,
                DatabaseContract.Profile._ID+"=?",
                new String[]{Integer.toString(1)});

    }
    private void destroyDatabase(){
        getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }


}
