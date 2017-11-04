package com.repandco.repco.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowHolder> {

    private String[] followList;


    @Override
    public FollowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FollowHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class FollowHolder extends RecyclerView.ViewHolder {

        public FollowHolder(View itemView) {
            super(itemView);
        }
    }
}
