package com.repandco.repco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }


    public void button1(View view) {
        Intent intent = new Intent(this, RegistrationActivity1.class);
        startActivity(intent);
    }

    public void button2(View view) {
        Intent intent = new Intent(this, RegistrationActivity1.class);
        startActivity(intent);
    }
}
