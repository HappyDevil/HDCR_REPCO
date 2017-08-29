package com.repandco.repco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.Values;
import com.repandco.repco.mainActivities.ScrollingActivity;
import com.repandco.repco.registActivity.RegistBusinessInfo;
import com.repandco.repco.registActivity.RegistUserInfo;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent = new Intent(this, ScrollingActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_first);
    }

    public void professional(View view) {
        Intent intent = new Intent(this, RegistUserInfo.class);
        intent.putExtra(Keys.TYPE, Values.TYPES.PROFESSIONAL_TYPE);
        startActivity(intent);
    }

    public void enterprise(View view) {
        Intent intent = new Intent(this, RegistBusinessInfo.class);
        intent.putExtra(Keys.TYPE, Values.TYPES.ENTERPRISE_TYPE);
        startActivity(intent);
    }
}
