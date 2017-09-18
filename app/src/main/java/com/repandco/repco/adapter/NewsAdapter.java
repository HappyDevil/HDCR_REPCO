package com.repandco.repco.adapter;

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
import com.repandco.repco.entities.News;
import com.repandco.repco.entities.StripeJobPost;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.repandco.repco.FirebaseConfig.mDatabase;

/**
 * Created by Даниил on 17.09.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private ArrayList<News> mDataset;
    private ManagerActivity manager;

    public NewsAdapter(ManagerActivity manager,ArrayList<News> mDataset) {
        this.manager = manager;
        this.mDataset = mDataset;
    }


    public static class NewsHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public TextView name;
        public TextView date;
        public TextView newsLabel;

        public NewsHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            newsLabel = (TextView) itemView.findViewById(R.id.newsLabel);
        }
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_news,parent,false);
        NewsHolder vh = new NewsHolder(v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(final NewsHolder holder, int position) {
        final News model = mDataset.get(position);
        if (model.getUid() !=null){

            switch (model.getType().intValue()){
                case Values.NEWS.DISFOLLOW:
                    holder.newsLabel.setText("disfollow you");
                    break;
                case Values.NEWS.FOLLOW:
                    holder.newsLabel.setText("follow you");
                    break;
                case Values.NEWS.FRIEND:
                    holder.newsLabel.setText("accept your friend-request");
                    break;
                case Values.NEWS.LIKE:
                    holder.newsLabel.setText("like your post");
                    break;
                case Values.NEWS.WORK:
                    holder.newsLabel.setText("accept your work-request");
                    break;
            }


            holder.date.setText(DateFormat.getTimeInstance().format(new java.util.Date(model.getDate())));
            mDatabase.getReference().child(URLS.USERS + model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        String name = dataSnapshot.child(Keys.NAME).getValue(String.class) + " " + dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
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
                            manager.openProfile(model.getUid());
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
