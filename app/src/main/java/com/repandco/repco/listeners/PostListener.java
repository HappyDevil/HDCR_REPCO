package com.repandco.repco.listeners;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.entities.StripeJobPost;


public class PostListener implements ValueEventListener {

    private PostAdapter postAdapter;

    public PostListener(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
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
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
