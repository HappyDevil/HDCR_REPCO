package com.repandco.repco;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.mainActivities.ScrollingActivity;
import com.repandco.repco.mainActivities.SearchActivity;
import com.repandco.repco.mainActivities.NotifActivity;
import com.repandco.repco.mainActivities.PostActivity;

public class BottomNavigationActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{


    private Intent postIntent;
    private Intent searchIntent;
    private Intent notifIntent;
    private Intent profileIntent;
    protected BottomNavigationView navigation;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postIntent = new Intent(this, PostActivity.class);
        searchIntent = new Intent(this, SearchActivity.class);
        notifIntent = new Intent(this, NotifActivity.class);
        profileIntent = new Intent(this, ScrollingActivity.class);

        mUser = FirebaseConfig.mAuth.getCurrentUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (mUser != null) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(postIntent);
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(searchIntent);
                    return true;
                case R.id.navigation_notifications:
                    startActivity(notifIntent);
                    return true;
                case R.id.navigation_profile:
                    profileIntent.putExtra(Keys.UID, mUser.getUid());
                    startActivity(profileIntent);
                    return true;
            }
        }
        return false;
    }

    public FirebaseUser getUser() {
        return mUser;
    }
}
