package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.smart_step_counter.data.DatabaseContract;

public class CreateGoalActivity extends AppCompatActivity {


    private Spinner spinner;
    private String[] goalTypes={"Расстояние","Шаги"};

    private EditText distance;
    private EditText steps;
    private EditText name;

    private Button createBtn;
    private Button cancelBtn;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private Handler mainHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        databaseHelper=new DatabaseHelper(this);
        setContentView(R.layout.activity_create_goal);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.goalSpinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Тип цели");
        // выделяем элемент
        //spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



        distance=findViewById(R.id.distanceEditTxt);
        steps=findViewById(R.id.stepsEditTxt);
        name=findViewById(R.id.goalNameTxt);

        cancelBtn=findViewById(R.id.cancellingGoalCreatingBtn);
        createBtn=findViewById(R.id.createGoalBtn);


        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateGoalActivity.this, AchievementsActivity.class));
            }
        });
       createBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               createGoal();
               startActivity(new Intent(CreateGoalActivity.this, AchievementsActivity.class));
           }
       });
    }


    private void createGoal(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection={
                        DatabaseContract.Goals._ID,
                        DatabaseContract.Goals.COLUMN_NAME
                };
                database=databaseHelper.getReadableDatabase();
                Cursor cursor2=database.query(
                        DatabaseContract.Goals.TABLE_NAME,
                        projection,null,null,null,null,null
                );
                if(cursor2.getCount()==2){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Максимальное число целей - 2" , Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    ContentValues contentValues=new ContentValues();
                    if(spinner.getSelectedItem().toString().equals("Расстояние"))
                        contentValues.put(DatabaseContract.Goals.COLUMN_TYPE,1);
                    else
                        contentValues.put(DatabaseContract.Goals.COLUMN_TYPE,0);
                    contentValues.put(DatabaseContract.Goals.COLUMN_STEPS, steps.getText().toString());
                    contentValues.put(DatabaseContract.Goals.COLUMN_DISTANCE, distance.getText().toString());
                    contentValues.put(DatabaseContract.Goals.COLUMN_NAME, name.getText().toString());
                    contentValues.put(DatabaseContract.Goals.COLUMN_DONE, 0);
                    database.insert(DatabaseContract.Goals.TABLE_NAME,null,contentValues);
                }
                cursor2.close();
            }
        }).start();


    }
}
