package com.repandco.repco;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
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
import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.VIDEOS;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_VIDEO;


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

    private Uri videoURI;
    private ImageView videoView;
    private String videoURL;


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
        videoView = (ImageView) findViewById(R.id.videoView);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_VIDEO);
            }
        });


        tags_list.setHasFixedSize(false);
        RecyclerView.LayoutManager  tagsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        tags_list.setLayoutManager(tagsLayoutManager);

        tagsAdapter = new TagsAdapter(manager,true);
        tagsAdapter.setTags_card(tags_card);
        tags_list.setAdapter(tagsAdapter);

        photos.setHasFixedSize(false);
        RecyclerView.LayoutManager  photosLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        photos.setLayoutManager(photosLayoutManager);

        imagesAdapter = new ImagesAdapter(new ArrayList<String>(),this,create);
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
        create.setActivated(true);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(create.isActivated()) {
                    String text_STR = text.getText().toString();
                    String title_STR = CreateComPost.this.title.getText().toString();
                    if(title_STR.isEmpty()){
                        title.setError("Title is empty");
                        return;
                    }
                    if(text_STR.isEmpty()){
                        text.setError("Text is empty");
                        return;
                    }

                    Post post = new Post();
                    post.setText(text_STR);
                    post.setTitle(title_STR);
                    post.setType((long) Values.POSTS.STANDARD_POST);
                    post.setUserid(mAuth.getCurrentUser().getUid());
                    post.setVideourl(videoURL);
                    post.setTags(tagsAdapter.getTags());
                    imagesAdapter.deletePhoto("PLUS");
                    post.setPhotos(imagesAdapter.getPhotos());
                    mDatabase.getReference().child(URLS.POSTS).push().setValue(post);
                    finish();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO) {
            if (data != null) {
                final Uri uri = data.getData();
                videoView.setBackgroundColor(getResources().getColor(R.color.cardtags2));
                videoURI = uri;
                if (videoURI != null) {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    //use one of overloaded setDataSource() functions to set your data source
                    retriever.setDataSource(CreateComPost.this, videoURI);
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);
                    retriever.release();
                    if (timeInMillisec > 60000) {
                        videoView.setBackgroundColor(getResources().getColor(R.color.addjobpost));
                        videoURI = null;
                        Toast.makeText(manager, "Error, File is too long", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        create.setActivated(false);
                        mStorage.getReference(VIDEOS).child(videoURI.getLastPathSegment()).putFile(videoURI)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        create.setActivated(true);
                                        if (task.isSuccessful()){
                                            Toast.makeText(CreateComPost.this, "Success", Toast.LENGTH_SHORT).show();
                                            videoURL = task.getResult().getMetadata().getDownloadUrl().toString();
                                        }
                                        else{
                                            Toast.makeText(CreateComPost.this, "Failed", Toast.LENGTH_SHORT).show();
                                            videoURL = null;
                                        }
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        create.setActivated(false);
                                        Toast.makeText(CreateComPost.this, "Loading video: "+((int)progress)+"%", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        }
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
