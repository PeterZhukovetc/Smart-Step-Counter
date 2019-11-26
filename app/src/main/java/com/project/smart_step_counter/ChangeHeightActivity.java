package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Toast;

public class ChangeHeightActivity extends AppCompatActivity {

    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_height);
        numberPicker=(NumberPicker) findViewById(R.id.numPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(250);
        numberPicker.setValue(160);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(onValueChangeListener);

    }

    NumberPicker.OnValueChangeListener onValueChangeListener=
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Toast.makeText(ChangeHeightActivity.this,
                            "selected"+numberPicker.getValue(),Toast.LENGTH_SHORT);
                }
            };


}
