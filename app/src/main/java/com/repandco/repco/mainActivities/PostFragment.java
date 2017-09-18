package com.repandco.repco.mainActivities;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.entities.StripeJobPost;
import com.repandco.repco.listeners.PostListener;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;


public class PostFragment extends Fragment {

    private ManagerActivity manager;
    private RecyclerView history;
    private LinearLayoutManager historyLayoutManager;
    private PostAdapter postAdapter;
    private StripeJobPost[] jobs;
    private Long curChunk = 0L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_home).setChecked(true);

        View content = inflater.inflate(R.layout.fragment_post_list, container, false);
        history = (RecyclerView) content.findViewById(R.id.history);

        history.setHasFixedSize(false);
        historyLayoutManager = new LinearLayoutManager(content.getContext(),LinearLayoutManager.VERTICAL,false);
        history.setLayoutManager(historyLayoutManager);
        postAdapter = new PostAdapter(manager);
        history.setAdapter(postAdapter);

        manager.cloudAPI.findFriendsPost(mAuth.getCurrentUser().getUid(), curChunk).enqueue(new Callback<StripeJobPost[]>() {
            @Override
            public void onResponse(Call<StripeJobPost[]> call, Response<StripeJobPost[]> response) {
                StripeJobPost[] jobs = response.body();
                if (jobs != null)
                    for (StripeJobPost post : jobs)
                        postAdapter.addNewPost(post);
            }

            @Override
            public void onFailure(Call<StripeJobPost[]> call, Throwable t) {

            }
        });

        history.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = historyLayoutManager.getChildCount();
                    totalItemCount = historyLayoutManager.getItemCount();
                    pastVisiblesItems = historyLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        curChunk++;
                        manager.cloudAPI.findFriendsPost(mAuth.getCurrentUser().getUid(), curChunk).enqueue(new Callback<StripeJobPost[]>() {
                            @Override
                            public void onResponse(Call<StripeJobPost[]> call, Response<StripeJobPost[]> response) {
                                StripeJobPost[] jobs = response.body();
                                if (jobs != null)
                                    for (StripeJobPost post : jobs)
                                        postAdapter.addNewPost(post);
                            }

                            @Override
                            public void onFailure(Call<StripeJobPost[]> call, Throwable t) {

                            }
                        });
                    }
                }
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

