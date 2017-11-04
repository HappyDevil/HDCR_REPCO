package com.repandco.repco.mainFragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;

public class NotifFragment extends Fragment {

    private ManagerActivity manager;
    private RecyclerView history;
    private LinearLayoutManager historyLayoutManager;
    private ProgressBar temp;
    private NewsAdapter newsAdapter;
    private final ArrayList<News> newses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_notifications).setChecked(true);

        View content = inflater.inflate(R.layout.fragment_notif, container, false);
        history = (RecyclerView) content.findViewById(R.id.history);
        temp = (ProgressBar) content.findViewById(R.id.temp);
        temp.setVisibility(View.VISIBLE);
        history.setHasFixedSize(false);
        historyLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false);
        history.setLayoutManager(historyLayoutManager);
        newses.clear();

        final DatabaseReference uidNotif = mDatabase.getReference().child(URLS.NOTIFICATIONS).child(mAuth.getCurrentUser().getUid());

        uidNotif.child(URLS.POSTS).child(URLS.LIKES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        String postID = data.getKey();
                        if(data.getValue()!=null) {
                            for (DataSnapshot userlike:data.getChildren()) {
                                News news = userlike.getValue(News.class);
                                news.setUid(userlike.getKey());
                                news.setPostID(postID);
                                news.setType((long) Values.NEWS.LIKE);
                                newses.add(news);
                            }
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
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        String postID = data.getKey();
                        if(data.getValue()!=null) {
                            for (DataSnapshot userlike:data.getChildren()) {
                                News news = userlike.getValue(News.class);
                                news.setUid(userlike.getKey());
                                news.setPostID(postID);
                                news.setType((long) Values.NEWS.WORK);
                                newses.add(news);
                            }
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
                    for (DataSnapshot data:dataSnapshot.getChildren()) {
                        News news = data.getValue(News.class);
                        news.setUid(data.getKey());
                        newses.add(news);
                    }
                }
                temp.setVisibility(View.GONE);
                newsAdapter = new NewsAdapter(manager,newses,temp);
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
