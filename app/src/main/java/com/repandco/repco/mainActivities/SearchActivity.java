package com.repandco.repco.mainActivities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;

import com.repandco.repco.BottomNavigationActivity;
import com.repandco.repco.R;


public class SearchActivity extends BottomNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_job_post);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


        navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
    }
}
