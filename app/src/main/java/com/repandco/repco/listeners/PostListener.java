package com.repandco.repco.listeners;

import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.entities.StripeJobPost;


public class PostListener implements ValueEventListener {

    private PostAdapter postAdapter;
    private TextView nopost;

    public PostListener(PostAdapter postAdapter,TextView nopost) {
        this.postAdapter = postAdapter;
        this.nopost = nopost;
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot data : dataSnapshot.getChildren()){
            StripeJobPost post = data.getValue(StripeJobPost.class);
            if(post!=null ){
                post.setPostid(data.getKey());
                postAdapter.addNewPost(post);
            }
        }
        if(nopost!=null)
            if(postAdapter.getItemCount()==0) nopost.setVisibility(View.VISIBLE);
            else nopost.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
