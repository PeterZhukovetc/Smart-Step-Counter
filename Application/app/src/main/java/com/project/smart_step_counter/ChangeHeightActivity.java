package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.project.smart_step_counter.data.DatabaseContract;

public class ChangeHeightActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private Button changeBtn;
    private Button cancelBtn;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_height);
        numberPicker=findViewById(R.id.numPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);
        numberPicker.setValue(160);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(onValueChangeListener);

        changeBtn=findViewById(R.id.changeHeightConfirmBtn);
        cancelBtn=findViewById(R.id.cancellingHeightChangeBtn);

        databaseHelper=new DatabaseHelper(this);


        changeBtn.setOnClickListener(v -> {
            changeHeight();
            startActivity(new Intent(ChangeHeightActivity.this, MainScreenActivity.class));
        });

        cancelBtn.setOnClickListener(v -> {
            startActivity(new Intent(ChangeHeightActivity.this, SettingsActivity.class));
        });

    }

    private void changeHeight(){
        database=databaseHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Profile.COLUMN_HEIGHT, numberPicker.getValue());
        database.update(DatabaseContract.Profile.TABLE_NAME,
                contentValues, DatabaseContract.Records._ID + "= ?", new String[]{"1"});

    }

    NumberPicker.OnValueChangeListener onValueChangeListener=
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Toast.makeText(ChangeHeightActivity.this,
                            "Выбран рост - "+numberPicker.getValue()+" см",Toast.LENGTH_SHORT);
                }
            };


}
