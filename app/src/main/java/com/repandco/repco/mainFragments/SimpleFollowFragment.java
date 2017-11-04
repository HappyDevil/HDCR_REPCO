package com.repandco.repco.mainFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repandco.repco.R;
import com.repandco.repco.adapter.FollowAdapter;

import java.util.ArrayList;

public class SimpleFollowFragment extends Fragment {

    private ArrayList<String> followed;

    public SimpleFollowFragment() {

    }

    public void setFollowed(ArrayList<String> followed) {
        this.followed = followed;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_follow, container, false);
        RecyclerView history = (RecyclerView) inflate.findViewById(R.id.history);
        history.setHasFixedSize(false);
        LinearLayoutManager historyLayoutManager = new LinearLayoutManager(inflate.getContext(), LinearLayoutManager.VERTICAL, false);
        history.setLayoutManager(historyLayoutManager);
        FollowAdapter followAdapter = new FollowAdapter(followed, inflate.getContext());
        history.setAdapter(followAdapter);
        return inflate;
    }

}
