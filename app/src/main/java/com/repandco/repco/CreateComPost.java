package com.repandco.repco;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.customClasses.LoadPhotoAct;
import com.repandco.repco.adapter.FindsAdapter;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.TagsAdapter;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.Post;

import java.util.ArrayList;
import java.util.TreeSet;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;


public class  CreateComPost extends LoadPhotoAct {
    private ManagerActivity manager;
    private EditText search;
    private RecyclerView finds;
    private boolean submit = false;
    private FindsAdapter findsAdapter;
    private RecyclerView tags_list;
    private RecyclerView photos;
    private TagsAdapter tagsAdapter;
    private CardView find_card;
    private CardView tags_card;
    private Button create;
    private EditText text;
    private Toolbar postTolbar;
    private EditText title;
    private ImagesAdapter imagesAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_com_post);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Create post");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        finds = (RecyclerView) findViewById(R.id.finds);
        search = (EditText) findViewById(R.id.search);
        text = (EditText) findViewById(R.id.text);
        title = (EditText) findViewById(R.id.title);
        tags_list = (RecyclerView) findViewById(R.id.tags_list);
        photos = (RecyclerView) findViewById(R.id.photos);
        create = (Button) findViewById(R.id.create);
        find_card = (CardView) findViewById(R.id.find_card);
        tags_card = (CardView) findViewById(R.id.tags_card);


        tags_list.setHasFixedSize(false);
        RecyclerView.LayoutManager  tagsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        tags_list.setLayoutManager(tagsLayoutManager);

        tagsAdapter = new TagsAdapter(manager,true);
        tagsAdapter.setTags_card(tags_card);
        tags_list.setAdapter(tagsAdapter);

        photos.setHasFixedSize(false);
        RecyclerView.LayoutManager  photosLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        photos.setLayoutManager(photosLayoutManager);

        imagesAdapter = new ImagesAdapter(new ArrayList<String>(),this);
        imagesAdapter.addPlus();
        photos.setAdapter(imagesAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String searchText = charSequence.toString();
                if(searchText.length()>0){
                    mDatabase.getReference().child(URLS.TAGS).orderByKey().startAt(searchText).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TreeSet<String> keys = new TreeSet<>();
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                String key = data.getKey();
                                if(!key.startsWith(searchText)) break;
                                keys.add(key);
                            }
                            findsAdapter.setFinds(keys);
                            if(!submit){
                                if(keys.size()>0) find_card.setVisibility(View.VISIBLE);
                            }
                            else submit = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            submit = true;
                            find_card.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    find_card.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        search.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findsAdapter.addNewTag(v.getText().toString());
                    return true;
                }
                return false;
            }
        });


        findsAdapter = new FindsAdapter(manager,true);
        findsAdapter.setTagsAdapter(tagsAdapter);
        finds.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        finds.setAdapter(findsAdapter);

        find_card.setVisibility(View.GONE);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post();
                post.setText(text.getText().toString());
                post.setTitle(title.getText().toString());
                post.setType((long) Values.POSTS.STANDARD_POST);
                post.setUserid(mAuth.getCurrentUser().getUid());
                post.setTags(tagsAdapter.getTags());
                imagesAdapter.deletePhoto("PLUS");
                post.setPhotos(imagesAdapter.getPhotos());
                mDatabase.getReference().child(URLS.POSTS).push().setValue(post);
                finish();
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
