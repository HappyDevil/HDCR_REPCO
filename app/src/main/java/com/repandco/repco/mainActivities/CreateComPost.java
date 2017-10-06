package com.repandco.repco.mainActivities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
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
import static com.repandco.repco.constants.Values.REQUEST.LOAD_POST_PHOTO;


public class CreateComPost extends AppCompatActivity {
    private ManagerActivity manager;
    private EditText search;
    private RecyclerView finds;
    private boolean submit = false;
    private FindsAdapter findsAdapter;
    private RecyclerView tags_list;
    private RecyclerView photos;
    private TagsAdapter adapter;
    private Button create;
    private EditText text;
    private Toolbar postTolbar;
    private EditText title;
    private ImagesAdapter imagesAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_create_com_post);

        finds = (RecyclerView) findViewById(R.id.finds);
        search = (EditText) findViewById(R.id.search);
        text = (EditText) findViewById(R.id.text);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        title = (EditText) findViewById(R.id.title);
        tags_list = (RecyclerView) findViewById(R.id.tags_list);
        photos = (RecyclerView) findViewById(R.id.photos);
        create = (Button) findViewById(R.id.create);

        postTolbar.setTitle("Create post");

        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tags_list.setHasFixedSize(false);
        RecyclerView.LayoutManager  tagsLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        tags_list.setLayoutManager(tagsLayoutManager);

        adapter = new TagsAdapter(manager,true);
        tags_list.setAdapter(adapter);

        photos.setHasFixedSize(false);
        RecyclerView.LayoutManager  photosLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        photos.setLayoutManager(photosLayoutManager);

        imagesAdapter = new ImagesAdapter(new ArrayList<String>(),manager);
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
                            TreeSet<String> keys = new TreeSet<String>();
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                String key = data.getKey();
                                if(!key.startsWith(searchText)) break;
                                keys.add(key);
                            }
                            findsAdapter.setFinds(keys);
                            if(!submit) finds.setVisibility(View.VISIBLE);
                            else submit = false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            submit = true;
                            finds.setVisibility(View.GONE);
                        }
                    });
                }
                else finds.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findsAdapter = new FindsAdapter(manager,true);
        findsAdapter.setTagsAdapter(adapter);
        finds.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        finds.setAdapter(findsAdapter);

        finds.setVisibility(View.GONE);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post();
                post.setText(text.getText().toString());
                post.setTitle(title.getText().toString());
                post.setType(Values.POSTS.STANDARD_POST);
                post.setUserid(mAuth.getCurrentUser().getUid());
                post.setTags(adapter.getTags());
                mDatabase.getReference().child(URLS.POSTS).push();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(imagesAdapter.plus!=null) imagesAdapter.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, LOAD_POST_PHOTO);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();

                if (requestCode == LOAD_POST_PHOTO){
                    imagesAdapter.plus.setImageURI(uri);
                }

//                mStorage.getReference(IMAGES).child(uri.getLastPathSegment()).putFile(uri)
//                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    intent.putExtra(intentUrl, task.getResult().getMetadata().getDownloadUrl().toString());
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                }
//                                else {
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                    photobut.setImageURI(null);
//                                }
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                                progressBar.setProgress((int) progress);
//                            }
//                        });
            }
        }
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }
}
