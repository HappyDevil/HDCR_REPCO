package com.repandco.repco.mainActivities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.repandco.repco.R;

public class ChoseType extends AppCompatActivity {

    private CardView info;
    private CardView job;
    private Context context;
    private Toolbar postTolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        info = (CardView) findViewById(R.id.info);
        job = (CardView) findViewById(R.id.job);
        context = this;

        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
