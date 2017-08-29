package com.repandco.repco.registActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.Toasts;
import com.repandco.repco.constants.Values;

public class RegistPersonalInfo extends AppCompatActivity {

    private EditText phone;
    private EditText birthdate;
    private RadioGroup radioGroup;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_personal_info);

        phone = (EditText) findViewById(R.id.phone);
        birthdate = (EditText) findViewById(R.id.birthdate);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        intent = getIntent();
    }

    public void next(View view) {
        if(intent!=null)
        {
            String s = birthdate.getText().toString();
            long date = Integer.valueOf(s);
            intent.putExtra(Keys.PHONE,phone.getText().toString());
            intent.putExtra(Keys.BIRTHDAY,date);
            switch (radioGroup.getCheckedRadioButtonId())
            {
                case R.id.male:
                    intent.putExtra(Keys.GENDER, Values.GENDERS.MALE);
                    break;
                case R.id.female:
                    intent.putExtra(Keys.GENDER, Values.GENDERS.FEMALE);
                    break;
                default:
                    Toast.makeText(this, Toasts.ERR_GENDER, Toast.LENGTH_SHORT).show();
            }
            intent.setClass(this,RegistAuthInfo.class);
        }
        else intent = new Intent(this, RegistUserInfo.class);

        startActivity(intent);
    }
}
