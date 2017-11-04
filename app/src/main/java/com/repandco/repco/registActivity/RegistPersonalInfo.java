package com.repandco.repco.registActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.Toasts;
import com.repandco.repco.constants.Values;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistPersonalInfo extends AppCompatActivity {

    private EditText phone;
    private RadioGroup radioGroup;
    private Intent intent;
    private Toolbar postTolbar;

    private long birthdayLONG;
    private TextView birthdaySTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_personal_info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Register auth info:");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        phone = (EditText) findViewById(R.id.phone);
        birthdaySTR = (TextView) findViewById(R.id.birthdaySTR);
        birthdayLONG = new Date().getTime();

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        intent = getIntent();
    }

    public void next(View view) {
        String phoneStr = phone.getText().toString();
        if(TextUtils.isEmpty(phoneStr)){
            phone.setError("Phone is empty!");
            phone.requestFocus();
            return;
        }
        else {
            if(phoneStr.length() < 4){
                phone.setError("Phone is wrong!");
                phone.requestFocus();
                return;
            }
        }
        if(intent!=null)
        {
            intent.putExtra(Keys.PHONE, phoneStr);
            intent.putExtra(Keys.BIRTHDAY,birthdayLONG);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDatePickerDialog(View view) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        DatePickerDialog newFragment = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mouth, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, mouth);
                cal.set(Calendar.DAY_OF_MONTH, day);
                birthdayLONG = cal.getTimeInMillis();

                birthdaySTR.setText(DateFormat.getDateInstance().format(cal.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        newFragment.show();
    }
}
