package com.repandco.repco.mainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.repandco.repco.BottomNavigationActivity;
import com.repandco.repco.R;

import static com.repandco.repco.R.id.navigation_home;


public class PostActivity extends BottomNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_info_post);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

}
