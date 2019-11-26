package com.project.smart_step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button exitSettingsBtn;
    private Button deleteProfileBtn;
    private Button changeHeightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        exitSettingsBtn=(Button) findViewById(R.id.exitSettingsBtn);
        exitSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });
        deleteProfileBtn=(Button) findViewById(R.id.deleteProfileBtn);
        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ConfirmingActivity.class);
                startActivity(intent);
            }
        });
        changeHeightBtn=(Button) findViewById(R.id.changeHeightBtn);
        changeHeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ChangeHeightActivity.class);
                startActivity(intent);
            }
        });
    }
}
