package com.repandco.repco.mainActivities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;

public class ChoseType extends Fragment {

    private CardView info;
    private CardView job;
    private ManagerActivity manager;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_add_post).setChecked(true);

        View content = inflater.inflate(R.layout.activity_add_post, container, false);

        info = (CardView) content.findViewById(R.id.info);
        job = (CardView) content.findViewById(R.id.job);
        context = content.getContext();

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIntent = new Intent(context, CreateComPost.class);
                startActivity(postIntent);
            }
        });

        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postIntent = new Intent(context, CreateComPost.class);
                startActivity(postIntent);
            }
        });
        return content;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
