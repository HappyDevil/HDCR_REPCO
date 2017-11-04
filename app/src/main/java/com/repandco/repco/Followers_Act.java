package com.repandco.repco;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.adapter.FollowAdapter;
import com.repandco.repco.adapter.FollowPagerAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.mainFragments.SimpleFollowFragment;

import java.util.ArrayList;

import static com.repandco.repco.constants.URLS.ChildURLS.FOLLOW;
import static com.repandco.repco.constants.URLS.ChildURLS.FOLLOWED;
import static com.repandco.repco.constants.URLS.FRIENDS;

public class Followers_Act extends AppCompatActivity {

    private ArrayList<String> followed;
    private ArrayList<String> follow;

    private ProgressBar progressPost;
    private Context context;
    private DatabaseReference reference;
    private String uid;
    private TabLayout tabs;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        uid = getIntent().getStringExtra(Keys.UID);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Create post");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        progressPost = (ProgressBar) findViewById(R.id.progressPost);
        progressPost.setVisibility(View.VISIBLE);
        tabs = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabs.setupWithViewPager(viewPager);
        context = this;
        progressPost.setVisibility(View.VISIBLE);

        reference = FirebaseConfig.mDatabase.getReference();
        setupViewPager(viewPager);


    }

    private boolean bolFollow = false;
    private boolean bolFollowed = false;

    private void setupViewPager(final ViewPager viewPager) {
        final FollowPagerAdapter adapter = new FollowPagerAdapter(getSupportFragmentManager());

        reference.child(FRIENDS+uid).child(FOLLOWED).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followed = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data != null) {
                        String key = data.getKey();
                        followed.add(key);
                    }
                }
                SimpleFollowFragment followed_frag = new SimpleFollowFragment();
                followed_frag.setFollowed(followed);
                adapter.addFragment(followed_frag, "Followed");
                bolFollow = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                bolFollow = true;
            }
        });
        reference.child(FRIENDS+uid).child(FOLLOW).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                follow = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data != null) follow.add(data.getKey());
                }
                SimpleFollowFragment followed_frag = new SimpleFollowFragment();
                followed_frag.setFollowed(follow);
                adapter.addFragment(followed_frag, "Follow");
                bolFollowed = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Internet error", Toast.LENGTH_SHORT).show();
                bolFollowed = true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(bolFollow&&bolFollowed) break;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressPost.setVisibility(View.GONE);
                        viewPager.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
