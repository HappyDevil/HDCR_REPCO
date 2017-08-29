package com.repandco.repco.registActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.repandco.repco.FirstActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;

public class RegistUserInfo extends AppCompatActivity {

    private Intent intent;
    private EditText name;
    private EditText firstname;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_user_info);

        intent = getIntent();

        name = (EditText) findViewById(R.id.name);
        firstname = (EditText) findViewById(R.id.firsname);
        email = (EditText) findViewById(R.id.emailaddress);

    }

    public void next(View view) {
        if(intent!=null)
        {
            intent.putExtra(Keys.NAME,name.getText().toString());
            intent.putExtra(Keys.FIRSTNAME,firstname.getText().toString());
            intent.putExtra(Keys.EMAIL,email.getText().toString());
            intent.setClass(this,RegistPersonalInfo.class);
        }
        else intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }
}
