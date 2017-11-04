package com.repandco.repco.adapter;

import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.StripeJobPost;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final ManagerActivity manager;
    private ArrayList<StripeJobPost> jobPosts;


    public static class PostHolder extends RecyclerView.ViewHolder {
        private final TextView typeText;
        public ImageView photo;
        public TextView name;
        public TextView date;
        private final ManagerActivity managerref;
        public TextView title;
        public TextView text;
        public TextView likes;
        public ImageView like;
        public ImageButton deletebut;
        public RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private RecyclerView tags_list;
        private ArrayList<String> photos;
        private String postID;
        private String uid;
        private View itemView;
        private boolean clicked = false;
        private boolean clickFinish = true;

        public void hideItems(boolean t){
            int visibilyty = (t) ? View.INVISIBLE : View.VISIBLE;
            photo.setVisibility(visibilyty);
            name.setVisibility(visibilyty);
            title.setVisibility(visibilyty);
            text.setVisibility(visibilyty);
            likes.setVisibility(visibilyty);
            date.setVisibility(visibilyty);
            like.setVisibility(visibilyty);
            typeText.setVisibility(visibilyty);
            mRecyclerView.setVisibility(visibilyty);
            tags_list.setVisibility(visibilyty);
        }

        public PostHolder(final View itemView,final ManagerActivity manager) {
            super(itemView);
//            itemView.setVisibility(View.INVISIBLE);

            this.itemView = itemView;

            managerref = manager;
            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
            title = (TextView) itemView.findViewById(R.id.title);
            text = (TextView) itemView.findViewById(R.id.text);
            likes = (TextView) itemView.findViewById(R.id.likes);
            date = (TextView) itemView.findViewById(R.id.date);
            like = (ImageView) itemView.findViewById(R.id.like);
            deletebut = (ImageButton) itemView.findViewById(R.id.deletebut);

            typeText = (TextView) itemView.findViewById(R.id.typeText);

            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.my_recycler_view);
            tags_list = (RecyclerView) itemView.findViewById(R.id.tags_list);

            mRecyclerView.setHasFixedSize(false);
            mLayoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            tags_list.setHasFixedSize(false);
            RecyclerView.LayoutManager  tagsLayoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            tags_list.setLayoutManager(tagsLayoutManager);

        }

        public ArrayList<String> getPhotos() {
            return photos;
        }

        public void setPhotos(ArrayList<String> photos) {
            this.photos = photos;
            if(photos!=null) {
                mAdapter = new ImagesAdapter(photos,managerref);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        public void setTags(TagsAdapter adapter) {
            tags_list.setAdapter(adapter);
        }

        public String getPostID() {
            return postID;
        }

        public void setPostID(final String postID) {
            this.postID = postID;

            mDatabase.getReference().child(URLS.LIKES+ postID +"/"+mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        clicked = true;
                        like.setImageResource(R.drawable.ic_hearth_click_24dp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clicked) {
                        mDatabase.getReference().child(URLS.LIKES + postID + "/" + mAuth.getCurrentUser().getUid()).removeValue();
                        clicked = false;
                        likes.setText(String.valueOf(Integer.valueOf((String) likes.getText()) - 1));
                        like.setImageResource(R.drawable.ic_hearth_24dp);
                    } else {
                        mDatabase.getReference().child(URLS.LIKES + postID + "/" + mAuth.getCurrentUser().getUid()).setValue(true);
                        clicked = true;
                        likes.setText(String.valueOf(Integer.valueOf((String) likes.getText()) + 1));
                        like.setImageResource(R.drawable.ic_hearth_click_24dp);
                    }
                }
            });
        }

        public String getUid() {
            return uid;
        }

        public void setUid(final String uid) {
            this.uid = uid;
            if(uid.equals(mAuth.getCurrentUser().getUid())) deletebut.setVisibility(View.VISIBLE);
        }


        public View getItemView() {
            return itemView;
        }
    }


    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_info_post,parent,false);
        PostHolder vh = new PostHolder(v1,manager);
        return vh;
    }

    @Override
    public void onBindViewHolder(final PostHolder holder, final int position) {
        final StripeJobPost model = jobPosts.get(position);

        if (model.getUserid() != null) {
            if(model.getDate()!=null) holder.date.setText(DateFormat.getDateTimeInstance().format(new Date(model.getDate())));
            holder.title.setText(model.getTitle());
            holder.text.setText(model.getText());
            holder.hideItems(true);

            if(holder.likes!=null) holder.likes.setText(String.valueOf(model.getLikes()));


            mDatabase.getReference().child(URLS.USERS + model.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        String firstName = dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
                        if(firstName==null)
                            firstName=null;
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
                            manager.openProfile(model.getUserid());
                        }
                    });
                    holder.setUid(model.getUserid());

                    holder.deletebut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String postid = model.getPostid();
                            String uid = model.getUserid();
                            mDatabase.getReference().child(URLS.POSTS+ postid).removeValue();
                            jobPosts.remove(postid);
                            notifyDataSetChanged();
                            manager.updateProfile(uid);
                        }
                    });
                    holder.setPostID(model.getPostid());
                    holder.setPhotos(model.getPhotos());

                    if(model.getType()!=Values.POSTS.STANDARD_POST){
                        holder.like.setVisibility(View.GONE);
                        holder.likes.setVisibility(View.GONE);
                        holder.typeText.setTextColor(holder.itemView.getResources().getColor(R.color.addinfopost));
                        holder.typeText.setBackground(holder.itemView.getResources().getDrawable(R.drawable.rounded));
                    }
                    switch (model.getType().intValue()){
                        case Values.POSTS.STANDARD_POST:
                            holder.typeText.setText("INFO");
                            holder.typeText.setTextColor(holder.itemView.getResources().getColor(R.color.addjobpost));
                            holder.typeText.setBackground(holder.itemView.getResources().getDrawable(R.drawable.rounded_info));
                            break;
                        case Values.POSTS.CDD_JOB_POST:
                            holder.typeText.setText("CDD");
                            break;
                        case Values.POSTS.CDI_JOB_POST:
                            holder.typeText.setText("CDI");
                            break;
                        case Values.POSTS.EXTRA_JOB_POST:
                            holder.typeText.setText("EXTRA");
                            break;
                    }

                    holder.setTags(new TagsAdapter(manager,model.getTags()));

                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            manager.openPost(model);
                        }
                    });
                    holder.hideItems(false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return jobPosts.size();
    }


    public PostAdapter(ManagerActivity manager) {
        this.manager = manager;
        jobPosts = new ArrayList<>();
    }

    public PostAdapter(ManagerActivity manager,ArrayList<StripeJobPost> jobPosts) {
        this.manager = manager;
        this.jobPosts = jobPosts;
    }

    public void addNewPost(StripeJobPost n){
        boolean contain = false;
        for (int i=0;i<jobPosts.size();i++)
            if(jobPosts.get(i).getPostid().equals(n.getPostid()))  contain = true;
        if(!contain) jobPosts.add(n);
        Collections.sort(jobPosts, new Comparator<StripeJobPost>() {
            @Override
            public int compare(final StripeJobPost object1, final StripeJobPost object2) {
                return ((object1.getDate()!=null) && (object2.getDate()!=null)) ? ((-1)*object1.getDate().compareTo(object2.getDate())) : 0;
            }
        });
        notifyDataSetChanged();
    }

    public void updatePost(StripeJobPost n){
        for (int i=0;i<jobPosts.size();i++)
            if(jobPosts.get(i).getPostid().equals(n.getPostid()))  jobPosts.set(i,n);
        notifyDataSetChanged();
    }

    public void removePost(StripeJobPost n){
        for (int i=0;i<jobPosts.size();i++)
            if(jobPosts.get(i).getPostid().equals(n.getPostid())) jobPosts.remove(i);
        notifyDataSetChanged();
    }

    public void removeAll(){
        for (int i=0;i<jobPosts.size();i++)
             jobPosts.remove(i);
        notifyDataSetChanged();
    }
}
