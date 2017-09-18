package com.repandco.repco.mainActivities;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.adapter.NewsAdapter;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.News;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;

public class NotifFragment extends Fragment {

    private ManagerActivity manager;
    private RecyclerView history;
    private LinearLayoutManager historyLayoutManager;
    private NewsAdapter newsAdapter;
    private final ArrayList<News> newses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_notifications).setChecked(true);

        View content = inflater.inflate(R.layout.fragment_notif, container, false);
        history = (RecyclerView) content.findViewById(R.id.history);

        history.setHasFixedSize(false);
        historyLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false);
        historyLayoutManager.offsetChildrenHorizontal(15);
        history.setLayoutManager(historyLayoutManager);

        final DatabaseReference uidNotif = mDatabase.getReference().child(URLS.NOTIFICATIONS).child(mAuth.getCurrentUser().getUid());

        uidNotif.child(URLS.POSTS).child(URLS.LIKES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String postID = dataSnapshot.getKey();
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        if(dataSnapshot.getValue()!=null) {
                            News news = dataSnapshot.getValue(News.class);
                            news.setUid(dataSnapshot.getKey());
                            news.setPostID(postID);
                            news.setType((long) Values.NEWS.LIKE);
                            newses.add(news);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        uidNotif.child(URLS.POSTS).child(URLS.ChildURLS.WORK).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String postID = dataSnapshot.getKey();
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        if(dataSnapshot.getValue()!=null) {
                            News news = dataSnapshot.getValue(News.class);
                            news.setUid(dataSnapshot.getKey());
                            news.setPostID(postID);
                            news.setType((long) Values.NEWS.WORK);
                            newses.add(news);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        uidNotif.child(URLS.FRIENDS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    News news = dataSnapshot.getValue(News.class);
                    news.setUid(dataSnapshot.getKey());
                    newses.add(news);
                }
                newsAdapter = new NewsAdapter(manager,newses);
                history.setAdapter(newsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
