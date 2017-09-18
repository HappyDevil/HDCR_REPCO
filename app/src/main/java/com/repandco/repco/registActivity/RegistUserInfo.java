package com.repandco.repco.registActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
            String nameStr = name.getText().toString();
            String firstnameStr = firstname.getText().toString();
            String emailStr = email.getText().toString();

            if(TextUtils.isEmpty(nameStr)){
                name.setError("Name is empty!");
                name.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(firstnameStr)){
                firstname.setError("Firstname is empty!");
                firstname.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(emailStr)){
                email.setError("Email is empty!");
                email.requestFocus();
                return;
            }
            else {
                if(!emailStr.contains("@")) {
                    email.setError("Email is wrong!");
                    email.requestFocus();
                    return;
                }
            }

            intent.putExtra(Keys.NAME, nameStr);
            intent.putExtra(Keys.FIRSTNAME, firstnameStr);
            intent.putExtra(Keys.EMAIL, emailStr);
            intent.setClass(this,RegistPersonalInfo.class);
        }
        else intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }
}
