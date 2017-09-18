package com.repandco.repco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.Values;
import com.repandco.repco.mainActivities.ProfileFragment;
import com.repandco.repco.registActivity.LoginActivity;
import com.repandco.repco.registActivity.RegistBusinessInfo;
import com.repandco.repco.registActivity.RegistUserInfo;

import static com.repandco.repco.FirebaseConfig.mAuth;

public class FirstActivity extends AppCompatActivity {

    private boolean login = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = new Intent(this, ManagerActivity.class);
            intent.putExtra(Keys.UID, mAuth.getCurrentUser().getUid());
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_first);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, ManagerActivity.class);
            intent.putExtra(Keys.UID, mAuth.getCurrentUser().getUid());
            startActivity(intent);
            finish();
        }
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

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        login = true;
    }
}
