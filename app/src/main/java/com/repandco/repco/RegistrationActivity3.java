package com.repandco.repco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration3);
    }

    public void button1(View view) {
        Intent intent = new Intent(this, ScrollingActivity.class);
        startActivity(intent);
    }
}
