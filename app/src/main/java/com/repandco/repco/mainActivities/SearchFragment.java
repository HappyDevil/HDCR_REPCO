package com.repandco.repco.mainActivities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.adapter.FindsAdapter;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.entities.StripeJobPost;

import java.util.Map;
import java.util.TreeSet;

import static com.repandco.repco.FirebaseConfig.mDatabase;


public class SearchFragment extends Fragment {

    private ManagerActivity manager;
    private PostAdapter postAdapter;
    private FindsAdapter findsAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private RecyclerView finds;
    private boolean submit = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_dashboard).setChecked(true);

        View content = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView)content.findViewById(R.id.search_view);
        progressBar = (ProgressBar) content.findViewById(R.id.progressBar);
        finds = (RecyclerView) content.findViewById(R.id.finds);

        searchView.setQueryHint("Search:");

        final ValueEventListener newPost = new ValueEventListener() {
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
                finds.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        };

        Bundle arguments = this.getArguments();
        String search = (arguments!=null) ? arguments.getString(Keys.SEARCH) : null;

//        searchView.setSuggestionsAdapter(new SimpleCursorAdapter(content.getContext()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                submit = true;
                finds.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                postAdapter.removeAll();
//                s = s.toLowerCase();
                if(s.length()>0){
                    mDatabase.getReference().child(URLS.TAGS).orderByKey().equalTo(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                if (!(data.getValue() instanceof Boolean)) {
                                    Map<String, Boolean> postID = (Map<String, Boolean>) data.getValue();
                                    if (postID.keySet() != null) {
                                        for (String key : postID.keySet()) {
                                            mDatabase.getReference().child(URLS.POSTS + key).addListenerForSingleValueEvent(newPost);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            finds.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final String searchText = s;
                if(s.length()>0){
                    mDatabase.getReference().child(URLS.TAGS).orderByKey().startAt(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TreeSet<String> keys = new TreeSet<String>();
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                String key = data.getKey();
                                if(!key.startsWith(searchText))break;
                                keys.add(key);
                            }
                            findsAdapter.setFinds(keys);
                            if(!submit) finds.setVisibility(View.VISIBLE);
                            else submit = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                return false;
            }
        });

        postAdapter = new PostAdapter(manager);

        RecyclerView history = (RecyclerView) content.findViewById(R.id.history);
        history.setHasFixedSize(false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false);
        history.setLayoutManager(historyLayoutManager);
        history.setAdapter(postAdapter);

        findsAdapter = new FindsAdapter(manager);
        finds.setLayoutManager(new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false));
        finds.setAdapter(findsAdapter);

        searchView.setIconified(false);
//        searchView.setFocusable(true);

        if(search!=null){
            searchView.setQuery(search,true);
        }
        finds.setVisibility(View.GONE);

        return content;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
