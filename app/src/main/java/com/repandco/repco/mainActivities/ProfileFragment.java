package com.repandco.repco.mainActivities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;

public class ProfileFragment extends Fragment {

    private ManagerActivity manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_profile).setChecked(true);
        return inflater.inflate(R.layout.activity_scrolling, container,false);
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }

}
