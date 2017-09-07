package com.repandco.repco.mainActivities;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;

public class NotifFragment extends Fragment {

    private ManagerActivity manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_notifications).setChecked(true);
        return inflater.inflate(R.layout.adapter_news, container,false);
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
