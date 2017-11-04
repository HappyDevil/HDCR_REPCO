package com.repandco.repco.adapter;

import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.OpenPost;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.Like;
import com.repandco.repco.request.CloudFuncAPI;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.repandco.repco.FirebaseConfig.mDatabase;

public class AcceptAdapter extends RecyclerView.Adapter<AcceptAdapter.AcceptHolder> {

    private Retrofit cloudFunctions;
    private CloudFuncAPI cloudAPI;
    private ArrayList<Like> likes;
    private OpenPost manager;

    public AcceptAdapter(ArrayList<Like> likes,OpenPost manager) {
        this.likes = likes;
        this.manager = manager;

        cloudFunctions = new Retrofit.Builder()
                .baseUrl(URLS.cloudFunc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cloudAPI = cloudFunctions.create(CloudFuncAPI.class);
    }

    public AcceptAdapter(OpenPost manager) {
        likes = new ArrayList<>();
        this.manager = manager;
    }

    @Override
    public AcceptHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_accept_job,parent,false);
        AcceptAdapter.AcceptHolder vh = new AcceptAdapter.AcceptHolder(v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AcceptHolder holder, final int position) {
        final Like model = likes.get(position);

        mDatabase.getReference().child(URLS.USERS + model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String firstName = dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
                    firstName = (firstName==null) ? "" : firstName;
                    String name = dataSnapshot.child(Keys.NAME).getValue(String.class) + " " + firstName;
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

                if(model.isAccept()) {
                    holder.ignore.setVisibility(View.GONE);
                    holder.accept.setText("Accepted");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.accept.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.accept.getContext(), R.color.cardtags2)));
                    }
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase.getReference().child(URLS.LIKES + model.getPostID()+ "/" + model.getUid()).setValue(false);
                            model.setAccept(false);
                            notifyDataSetChanged();
                        }
                    });
                }
                else {
                    holder.ignore.setVisibility(View.VISIBLE);
                    holder.accept.setText("Accept");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.accept.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.accept.getContext(), R.color.addjobpost)));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.accept.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.accept.getContext(), R.color.cardtags2)));
                    }
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cloudAPI.developNotReq(model.getPostID(),model.getUid());
                            mDatabase.getReference().child(URLS.LIKES + model.getPostID()+ "/" + model.getUid()).setValue(true);
                            model.setAccept(true);
                            notifyDataSetChanged();
                        }
                    });

                    holder.ignore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase.getReference().child(URLS.LIKES + model.getPostID()).child(model.getUid()).setValue(0);
                            likes.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    public class AcceptHolder extends RecyclerView.ViewHolder {

        public ImageView photo;
        public TextView name;
        public Button accept;
        public Button ignore;

        public AcceptHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
            accept = (Button) itemView.findViewById(R.id.accept);
            ignore = (Button) itemView.findViewById(R.id.ignore);

        }
    }
}
