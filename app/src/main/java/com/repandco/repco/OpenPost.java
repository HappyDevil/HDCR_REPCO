package com.repandco.repco;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.adapter.AcceptAdapter;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.TagsAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.Like;
import com.repandco.repco.entities.StripeJobPost;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;

public class OpenPost extends AppCompatActivity {

    public Activity act;
    public ImageView photo;
    public TextView name;
    public TextView date;
    public TextView title;
    public TextView price;
    public TextView currency;
    public TextView text;
    public TextView typeText;
    public TextView subCategory;
    public TextView canditates;
    public TextView category;
    public TextView commission;
    public TextView subProfession;
    public TextView profession;
    public TextView likes;
    public TextView text_apply;
    public TextView novideo;
    private LinearLayout money_info;
    public ImageView like;
    public ImageView videoView;
    public ImageButton deletebut;
    public RecyclerView mRecyclerView;
    public RecyclerView accept_jobs;
    private ImagesAdapter mAdapter;
    private AcceptAdapter acceptAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView tags_list;
    private String postID;
    private boolean clicked = false;
    private boolean clickFinish = true;
    private Context context;

    private CardView apply;

    private StripeJobPost model;
    private Long type;
    private Toolbar postTolbar;
    private LinearLayoutManager accept_manager;

    public OpenPost() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_post);

        act = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Rep&Co");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent postIntent = getIntent();

        model = new StripeJobPost();

        model.setText(postIntent.getStringExtra(Keys.TEXT));
        model.setCommission(postIntent.getIntExtra(Keys.COMMISSION,0));
        model.setCurrency(postIntent.getStringExtra(Keys.CURRENCY));
        model.setPrice(postIntent.getIntExtra(Keys.PRICE,0));
        model.setCategory(postIntent.getStringExtra(Keys.CATEGORY));
        model.setDate(postIntent.getLongExtra(Keys.DATE,0));
        model.setLikes(postIntent.getLongExtra(Keys.LIKES,0));
        model.setPhotos(postIntent.getStringArrayListExtra(Keys.PHOTOS));
        model.setPostid(postIntent.getStringExtra(Keys.POSTID));
        model.setProfession(postIntent.getStringExtra(Keys.PROFESSION));
        model.setTitle(postIntent.getStringExtra(Keys.TITLE));
        model.setType(postIntent.getLongExtra(Keys.TYPE,0));
        model.setUserid(postIntent.getStringExtra(Keys.UID));
        model.setText(postIntent.getStringExtra(Keys.TEXT));
        model.setVideourl(postIntent.getStringExtra(Keys.VIDEO));

        HashMap<String,Boolean> stringHashMap = new HashMap<>();

        postID = model.getPostid();
        type = model.getType();

        String[] stringArrayExtra = postIntent.getStringArrayExtra(Keys.TAGS);
        for(String tag : stringArrayExtra){
            stringHashMap.put(tag,true);
        }
        model.setTags(stringHashMap);

        context = this;
        novideo = (TextView) findViewById(R.id.novideo);
        photo = (ImageView) findViewById(R.id.photo);
        money_info = (LinearLayout) findViewById(R.id.money_info);
        name = (TextView) findViewById(R.id.name);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        likes = (TextView) findViewById(R.id.likes);
        date = (TextView) findViewById(R.id.date);
        like = (ImageView) findViewById(R.id.like);
        videoView = (ImageView) findViewById(R.id.videoView);
        deletebut = (ImageButton) findViewById(R.id.deletebut);
        apply = (CardView) findViewById(R.id.apply);
        currency = (TextView) findViewById(R.id.currency);
        price = (TextView) findViewById(R.id.price);
        commission = (TextView) findViewById(R.id.commission);
        canditates = (TextView) findViewById(R.id.canditates);

        typeText = (TextView) findViewById(R.id.typeText);
        subCategory = (TextView) findViewById(R.id.subCategory);
        category = (TextView) findViewById(R.id.category);
        subProfession = (TextView) findViewById(R.id.subProfession);
        profession = (TextView) findViewById(R.id.profession);
        text_apply = (TextView) findViewById(R.id.text_apply);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        accept_jobs = (RecyclerView) findViewById(R.id.accept_jobs);
        tags_list = (RecyclerView) findViewById(R.id.tags_list);

        if(model.getVideourl()!=null) {
            novideo.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openVideo(model.getVideourl(), context);
                }
            });
        }
        else {
            novideo.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }

        mRecyclerView.setHasFixedSize(false);
        accept_jobs.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        accept_manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        accept_jobs.setLayoutManager(accept_manager);

        tags_list.setHasFixedSize(false);
        RecyclerView.LayoutManager  tagsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        tags_list.setLayoutManager(tagsLayoutManager);

        if (model != null) {
            date.setText(DateFormat.getDateTimeInstance().format(new Date(model.getDate())));
            title.setText(model.getTitle());
            text.setText(model.getText());

            if(likes!=null) likes.setText(String.valueOf(model.getLikes()));


            mDatabase.getReference().child(URLS.USERS + model.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        String firstName = dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
                        String userName = dataSnapshot.child(Keys.NAME).getValue(String.class);
                        firstName = (firstName!=null) ? firstName : "";
                        firstName = upperCaseFirstLetter(firstName);
                        userName = upperCaseFirstLetter(userName);
                        String nameSTR = userName + " " + firstName;
                        name.setText(nameSTR);
                        String photourl = (String) dataSnapshot.child(Keys.PHOTO).getValue();

                        if(photourl!=null){
                            if(!photourl.equals( Values.URLS.STANDARD)){
                                Picasso.with(context)
                                        .load(photourl)
                                        .into(photo);
                            }
                        }
                    }
                    photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openProfile(model.getUserid());
                        }
                    });

//                    holder.deletebut.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String postid = model.getPostid();
//                            mDatabase.getReference().child(URLS.POSTS+ postid).removeValue();
//                            jobPosts.remove(postid);
//                            notifyItemChanged(position);
//                        }
//                    });

                    mDatabase.getReference().child(URLS.LIKES+ postID +"/"+mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null){
                                clicked = true;
                                like.setImageResource(R.drawable.ic_hearth_click_24dp);
                                text_apply.setText("Applyed");

                                apply.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(apply.getContext(), R.color.cardtags2)));
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

                    if(model.getUserid().equals(mAuth.getCurrentUser().getUid())){
                        mDatabase.getReference().child(URLS.LIKES+ postID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<Like> likes = new ArrayList<>();
                                for (DataSnapshot data:dataSnapshot.getChildren()) {
                                    if(data.getValue() instanceof Boolean){
                                        Like like = new Like();
                                        like.setAccept((Boolean) data.getValue());
                                        like.setPostID(postID);
                                        like.setUid(data.getKey());
                                        likes.add(like);
                                    }
                                }
                                acceptAdapter = new AcceptAdapter(likes,OpenPost.this);
                                accept_jobs.setAdapter(acceptAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        apply.setVisibility(View.GONE);
                    }
                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clicked) {
                                mDatabase.getReference().child(URLS.LIKES + postID + "/" + mAuth.getCurrentUser().getUid()).removeValue();
                                clicked = false;
                                text_apply.setText("Apply Job");
                                apply.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(apply.getContext(), R.color.addjobpost)));
                            } else {
                                mDatabase.getReference().child(URLS.LIKES + postID + "/" + mAuth.getCurrentUser().getUid()).setValue(false);
                                clicked = true;
                                text_apply.setText("Applyed");
                                apply.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(apply.getContext(), R.color.cardtags2)));
                            }
                        }
                    });
                    if(model.getPhotos()!=null) {
                        mAdapter = new ImagesAdapter(model.getPhotos(),null,null);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    tags_list.setAdapter(new TagsAdapter(act,model.getTags()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            if(type!=Values.POSTS.STANDARD_POST){
                category.setText(model.getCategory());
                profession.setText(profession.getText());

                if(model.getUserid().equals(mAuth.getCurrentUser().getUid())){
                    accept_jobs.setVisibility(View.VISIBLE);
                    canditates.setVisibility(View.VISIBLE);
                }

                like.setVisibility(View.GONE);
                likes.setVisibility(View.GONE);
            }
            switch (type.intValue()){
                case Values.POSTS.STANDARD_POST:
                    typeText.setText("INFO");
                    typeText.setTextColor(getResources().getColor(R.color.addjobpost));
                    typeText.setBackground(getResources().getDrawable(R.drawable.rounded_info));

                    apply.setVisibility(View.GONE);

                    subCategory.setVisibility(View.GONE);
                    subProfession.setVisibility(View.GONE);
                    profession.setVisibility(View.GONE);
                    category.setVisibility(View.GONE);
                    break;
                case Values.POSTS.CDD_JOB_POST:
                    typeText.setText("CDD");
                    break;
                case Values.POSTS.CDI_JOB_POST:
                    typeText.setText("CDI");
                    break;
                case Values.POSTS.EXTRA_JOB_POST:
                    typeText.setText("EXTRA");
                    money_info.setVisibility(View.VISIBLE);
                    currency.setText(model.getCurrency());
                    commission.setText(String.valueOf(model.getCommission()));
                    price.setText(String.valueOf(model.getPrice()));
                    break;
            }

        }
    }

    public void openVideo(String videourl, Context context) {
        if (videourl != null) {

            final Dialog builder = new Dialog(context, R.style.Theme_AppCompat);
//            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            builder.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.darkTransp)));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    builder.hide();
                }
            });
            LayoutInflater inflater = getLayoutInflater();
            final View inflate = inflater.inflate(R.layout.item_video_view, null);

            final VideoView videoView = (VideoView) inflate.findViewById(R.id.videoView);

            Uri video = Uri.parse(videourl);
            videoView.setVideoURI(video);

            MediaController mc = new MediaController(OpenPost.this);
            videoView.setMediaController(mc);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);

            ((ViewGroup) mc.getParent()).removeView(mc);

            ((FrameLayout) inflate.findViewById(R.id.videoViewWrapper))
                    .addView(mc);
            mc.setVisibility(View.VISIBLE);

            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.hide();
                }
            });

            builder.setContentView(inflate);

            builder.show();
            videoView.start();
        }
    }

    public void openProfile(String uid) {
        Intent intent = new Intent(context, ManagerActivity.class);
        intent.putExtra(Keys.UID,uid);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
