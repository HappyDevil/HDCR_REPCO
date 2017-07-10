package com.repandco.repco;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static com.repandco.repco.R.id.navigation_home;

public class ScrollingActivity extends AppCompatActivity {

    private Intent inte;
    private Intent inte1;
    private Intent inte2;
    private Intent inte3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Android Studio");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);

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
                    startActivity(inte2);
                    finish();
                    return true;
                case R.id.navigation_profile:
                    return true;
            }
            return false;
        }

    };
}
