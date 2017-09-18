package com.repandco.repco.listeners;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.entities.StripeJobPost;


public class PostListener implements ChildEventListener {

    private PostAdapter postAdapter;

    public PostListener(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        StripeJobPost post = dataSnapshot.getValue(StripeJobPost.class);
        if(post!=null ){
            post.setPostid(dataSnapshot.getKey());
            postAdapter.addNewPost(post);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        StripeJobPost post = dataSnapshot.getValue(StripeJobPost.class);
        if(post!=null ){
            post.setPostid(dataSnapshot.getKey());
            postAdapter.updatePost(post);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        StripeJobPost post = dataSnapshot.getValue(StripeJobPost.class);
        if(post!=null ){
            post.setPostid(dataSnapshot.getKey());
            postAdapter.removePost(post);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
