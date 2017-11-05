package com.repandco.repco.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.Like;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.repandco.repco.FirebaseConfig.mDatabase;


public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowHolder> {

    private ArrayList<String> followList;
    private Context context;

    public FollowAdapter(ArrayList<String> followList, Context context) {
        this.followList = followList;
        this.context = context;
    }

    @Override
    public FollowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_followers,parent,false);
        FollowAdapter.FollowHolder vh = new FollowAdapter.FollowHolder(v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FollowHolder holder, int position) {
        final String model = followList.get(position);

        mDatabase.getReference().child(URLS.USERS + model).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String firstName = dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
                    String userName = dataSnapshot.child(Keys.NAME).getValue(String.class);

                    firstName = upperCaseFirstLetter(firstName);
                    userName = upperCaseFirstLetter(userName);
                    String name = userName + " " + firstName;
                    holder.name.setText(name);
                    String photourl = (String) dataSnapshot.child(Keys.PHOTO).getValue();

                    if(photourl!=null){
                        if(!photourl.equals( Values.URLS.STANDARD)){
                            Picasso.with(holder.itemView.getContext())
                                    .load(photourl)
                                    .into(holder.photo);
                        }
                    }
                }
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openProfile(model);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }

    public void openProfile(String uid) {
        Intent intent = new Intent(context, ManagerActivity.class);
        intent.putExtra(Keys.UID,uid);
        context.startActivity(intent);
    }



    public static class FollowHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public TextView name;

        public FollowHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    private String upperCaseFirstLetter(String name){
        if(name!=null) {
            String s1 = name.substring(0, 1).toUpperCase();
            name = s1 + name.substring(1);
        }
        else name = "";
        return name;
    }
}
