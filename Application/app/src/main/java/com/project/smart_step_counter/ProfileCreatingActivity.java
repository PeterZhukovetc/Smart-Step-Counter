package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileCreatingActivity extends AppCompatActivity  {

    private NumberPicker numberPicker;
    private TextView textView;
    private Button createProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView) findViewById(R.id.textView2);
        numberPicker=(NumberPicker) findViewById(R.id.numPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);
        numberPicker.setValue(160);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        createProfileBtn =  findViewById(R.id.createProfileBtn);
        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileCreatingActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });

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
