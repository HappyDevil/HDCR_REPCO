package com.repandco.repco;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Intent inte;
    private Intent inte1;
    private Intent inte2;
    private Intent inte3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                case R.id.navigation_home:
                    startActivity(inte);
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(inte1);
                    return true;
                case R.id.navigation_notifications:
                    startActivity(inte2);
                    return true;
                case R.id.navigation_profile:
                    startActivity(inte3);
                    return true;
            }
            return false;
        }

    };

}
