package com.repandco.repco.registActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;

public class RegistBusinessContacts extends AppCompatActivity {

    private EditText address;
    private EditText emailaddress;
    private EditText phone;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_business_contacts);

        intent = getIntent();

        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        emailaddress = (EditText) findViewById(R.id.emailaddress);
    }

    public void next(View view) {
        if(intent!=null)
        {
            String addressStr = address.getText().toString();
            String phoneStr = phone.getText().toString();
            String emailStr = emailaddress.getText().toString();

            if(TextUtils.isEmpty(phoneStr)){
                phone.setError("Phone is empty!");
                phone.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(emailStr)){
                emailaddress.setError("Email is empty!");
                emailaddress.requestFocus();
                return;
            }
            else {
                if(!emailStr.contains("@")) {
                    emailaddress.setError("Email is wrong!");
                    emailaddress.requestFocus();
                    return;
                }
            }
            if(TextUtils.isEmpty(addressStr)){
                address.setError("Address is empty!");
                address.requestFocus();
                return;
            }

            intent.putExtra(Keys.ADDRESS, addressStr);
            intent.putExtra(Keys.PHONE, phoneStr);
            intent.putExtra(Keys.EMAIL, emailStr);
            intent.setClass(this,RegistAuthInfo.class);
        }
        else intent = new Intent(this, RegistBusinessInfo.class);
        startActivity(intent);
    }
}
