package com.repandco.repco;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import static com.repandco.repco.R.id.navigation_home;

public class NotifActivity extends AppCompatActivity {

    private Intent inte;
    private Intent inte1;
    private Intent inte2;
    private Intent inte3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_news);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_notifications).setChecked(true);

        Intent intent = new Intent(this, PostActivity.class);
        Intent intent1 = new Intent(this, SearchActivity.class);
        Intent intent2 = new Intent(this, NotifActivity.class);
        Intent intent3 = new Intent(this, ScrollingActivity.class);
        inte = intent;
        inte1 = intent1;
        inte2 = intent2;
        inte3 = intent3;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case navigation_home:
                    startActivity(inte);
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(inte1);
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    return true;
                case R.id.navigation_profile:
                    startActivity(inte3);
                    finish();
                    return true;
            }
            return false;
        }

    };
}
