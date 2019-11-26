package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateGoalActivity extends AppCompatActivity {

    private Spinner goalTypeSpinner;
    private String[] goalTypes={"Расстояние","Шаги"};
    private EditText distance;
    private EditText steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.goalSpinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Тип цели");
        // выделяем элемент
        //spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        distance=(EditText)findViewById(R.id.distanceEditTxt);
        steps=(EditText)findViewById(R.id.stepsEditTxt);
       // distance.set
    }
}
