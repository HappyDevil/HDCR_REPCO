package com.repandco.repco;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.repandco.repco.adapter.FollowAdapter;

public class Followers_Act extends AppCompatActivity {

    private Toolbar postTolbar;
    private ManagerActivity manager;
    private RecyclerView history;
    private LinearLayoutManager historyLayoutManager;
    private FollowAdapter followAdapter;
    private String[] follows;
    private Long curChunk = 0L;
    private ProgressBar progressPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Rep&Co");
        setSupportActionBar(postTolbar);

        Intent postIntent = getIntent();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
