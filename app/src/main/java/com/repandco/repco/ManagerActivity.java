package com.repandco.repco;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.repandco.repco.constants.Keys;
import com.repandco.repco.mainActivities.NotifFragment;
import com.repandco.repco.mainActivities.PostFragment;
import com.repandco.repco.mainActivities.ProfileFragment;
import com.repandco.repco.mainActivities.SearchFragment;

import static com.repandco.repco.FirebaseConfig.mAuth;

public class ManagerActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fTrans = getFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setManager(this);

        Bundle bundle = new Bundle();
        bundle.putString(Keys.UID, mAuth.getCurrentUser().getUid());
        profileFragment.setArguments(bundle);

        fTrans.add(R.id.frgmCont, profileFragment);
        fTrans.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(!item.isChecked()) {
            fTrans = getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    PostFragment postFragment = new PostFragment();
                    postFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, postFragment);
                    break;
                case R.id.navigation_dashboard:
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, searchFragment);
                    break;
                case R.id.navigation_notifications:
                    NotifFragment notifFragment = new NotifFragment();
                    notifFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, notifFragment);
                    break;
                case R.id.navigation_profile:
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setManager(this);

                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.UID, mAuth.getCurrentUser().getUid());
                    profileFragment.setArguments(bundle);

                    fTrans.replace(R.id.frgmCont, profileFragment);
                    break;
            }
            fTrans.addToBackStack(null);
            fTrans.commit();
            return true;
        }
        return false;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }
}
