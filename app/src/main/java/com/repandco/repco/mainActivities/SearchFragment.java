package com.repandco.repco.mainActivities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.entities.StripeJobPost;

import java.util.Map;
import java.util.TreeSet;

import static com.repandco.repco.FirebaseConfig.mDatabase;


public class SearchFragment extends Fragment {

    private ManagerActivity manager;
    private RecyclerView history;
    private LinearLayoutManager historyLayoutManager;
    private PostAdapter postAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_dashboard).setChecked(true);

        View content = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = (SearchView)content.findViewById(R.id.search_view);
        searchView.setQueryHint("Search:");

//        searchView.setSuggestionsAdapter(new SimpleCursorAdapter(content.getContext()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s.length()>0){
                    mDatabase.getReference().child(URLS.TAGS).orderByKey().equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                Map<String,Boolean> postID= (Map<String, Boolean>) data.getValue();
                                if(postID.keySet()!=null) {
                                    for (String key : postID.keySet()) {
                                        mDatabase.getReference().child(URLS.POSTS + key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                StripeJobPost post = dataSnapshot.getValue(StripeJobPost.class);
                                                if (post != null) {
                                                    post.setPostid(dataSnapshot.getKey());
                                                    postAdapter.addNewPost(post);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()>0){
                    mDatabase.getReference().child(URLS.TAGS).orderByKey().startAt(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TreeSet<String> keys = new TreeSet<String>();
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                String key = data.getKey();
                                keys.add(key);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                return false;
            }
        });


        history = (RecyclerView) content.findViewById(R.id.history);

        history.setHasFixedSize(false);
        historyLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false);
        historyLayoutManager.offsetChildrenHorizontal(15);
        history.setLayoutManager(historyLayoutManager);
        postAdapter = new PostAdapter(manager);
        history.setAdapter(postAdapter);

        return content;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
