package com.repandco.repco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.TagsAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.StripeJobPost;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
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
    public TextView category;
    public TextView commission;
    public TextView subProfession;
    public TextView profession;
    public TextView likes;
    public TextView text_apply;
    private LinearLayout money_info;
    public ImageView like;
    public ImageButton deletebut;
    public RecyclerView mRecyclerView;
    public RecyclerView accept_jobs;
    private ImagesAdapter mAdapter;
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

        HashMap<String,Boolean> stringHashMap = new HashMap<>();

        type = model.getType();

        String[] stringArrayExtra = postIntent.getStringArrayExtra(Keys.TAGS);
        for(String tag : stringArrayExtra){
            stringHashMap.put(tag,true);
        }
        model.setTags(stringHashMap);

        context = this;
        photo = (ImageView) findViewById(R.id.photo);
        money_info = (LinearLayout) findViewById(R.id.money_info);
        name = (TextView) findViewById(R.id.name);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        likes = (TextView) findViewById(R.id.likes);
        date = (TextView) findViewById(R.id.date);
        like = (ImageView) findViewById(R.id.like);
        deletebut = (ImageButton) findViewById(R.id.deletebut);
        apply = (CardView) findViewById(R.id.apply);
        currency = (TextView) findViewById(R.id.currency);
        price = (TextView) findViewById(R.id.price);
        commission = (TextView) findViewById(R.id.commission);

        typeText = (TextView) findViewById(R.id.typeText);
        subCategory = (TextView) findViewById(R.id.subCategory);
        category = (TextView) findViewById(R.id.category);
        subProfession = (TextView) findViewById(R.id.subProfession);
        profession = (TextView) findViewById(R.id.profession);
        text_apply = (TextView) findViewById(R.id.text_apply);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        accept_jobs = (RecyclerView) findViewById(R.id.accept_jobs);
        tags_list = (RecyclerView) findViewById(R.id.tags_list);

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
                        String nameStr = dataSnapshot.child(Keys.NAME).getValue(String.class) + " " + dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
                        name.setText(nameStr);
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
                            Intent intent = new Intent(context, ManagerActivity.class);
                            intent.putExtra(Keys.UID,model.getUserid());
                            startActivity(intent);
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
                                apply.setCardBackgroundColor(getResources().getColor(R.color.cardtags2));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mDatabase.getReference().child(URLS.LIKES+ postID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

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

                    if(model.getUserid().equals(mAuth.getCurrentUser().getUid())) apply.setVisibility(View.GONE);
                    apply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clicked) {
                                mDatabase.getReference().child(URLS.LIKES + postID + "/" + mAuth.getCurrentUser().getUid()).removeValue();
                                clicked = false;
                                text_apply.setText("Apply Job");
                                apply.setCardBackgroundColor(getResources().getColor(R.color.addjobpost));
                            } else {
                                mDatabase.getReference().child(URLS.LIKES + postID + "/" + mAuth.getCurrentUser().getUid()).setValue(false);
                                clicked = true;
                                text_apply.setText("Applyed");
                                apply.setCardBackgroundColor(getResources().getColor(R.color.cardtags2));
                            }
                        }
                    });
                    if(model.getPhotos()!=null) {
                        mAdapter = new ImagesAdapter(model.getPhotos(),null);
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
