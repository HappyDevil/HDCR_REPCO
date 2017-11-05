package com.repandco.repco;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.repandco.repco.adapter.FindsAdapter;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.TagsAdapter;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.customClasses.LoadPhotoAct;
import com.repandco.repco.entities.StripeJobPost;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;
import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.VIDEOS;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_VIDEO;


public class CreateJobPost extends LoadPhotoAct {

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
    private LinearLayout priceLayout;

    private DatabaseReference reference;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private Spinner category;
    private Spinner proffesion;
    private Spinner currency;
    private EditText price;

    private Uri videoURI;
    private ImageView videoView;
    private String videoURL;

    private int cdd_but_id,cdi_but_id,extra_but_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_post);

        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Create post");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reference = FirebaseConfig.mDatabase.getReference();

        finds = (RecyclerView) findViewById(R.id.finds);
        search = (EditText) findViewById(R.id.search);
        text = (EditText) findViewById(R.id.text);
        title = (EditText) findViewById(R.id.title);
        tags_list = (RecyclerView) findViewById(R.id.tags_list);
        photos = (RecyclerView) findViewById(R.id.photos);
        create = (Button) findViewById(R.id.create);
        find_card = (CardView) findViewById(R.id.find_card);
        tags_card = (CardView) findViewById(R.id.tags_card);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton = (RadioButton) findViewById(R.id.radioButtonCDD);
        radioButton1 = (RadioButton) findViewById(R.id.radioButtonCDI);
        radioButton2 = (RadioButton) findViewById(R.id.radioButtonEXTRA);

        cdd_but_id = radioButton.getId();
        cdi_but_id = radioButton1.getId();
        extra_but_id = radioButton2.getId();


        category = (Spinner) findViewById(R.id.spinner);
        proffesion = (Spinner) findViewById(R.id.spinner5);
        currency = (Spinner) findViewById(R.id.spinner6);
        price = (EditText) findViewById(R.id.editText);

        priceLayout = (LinearLayout) findViewById(R.id.price);
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
        final Context context = this;

        // Настраиваем адаптер
        reference.child(URLS.CURRENCY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listData = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                    listData.add(data.getKey());
                String[] stringList = new String[listData.size()];
                stringList = listData.toArray(stringList);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                currency.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.child(URLS.CATEGORY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listData = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                    listData.add(data.getKey());
                String[] stringList = new String[listData.size()];
                stringList = listData.toArray(stringList);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                category.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.child(URLS.JOB).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listData = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                    listData.add(data.getKey());
                String[] stringList = new String[listData.size()];
                stringList = listData.toArray(stringList);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                proffesion.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int num = Values.POSTS.CDD_JOB_POST;
                if(i == cdd_but_id) num = Values.POSTS.CDD_JOB_POST;
                if(i == cdi_but_id) num = Values.POSTS.CDI_JOB_POST;
                if(i == extra_but_id) num = Values.POSTS.EXTRA_JOB_POST;
                if(num>2){
                    priceLayout.setVisibility(View.VISIBLE);
                }
                else priceLayout.setVisibility(View.GONE);
            }
        });

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
                    StripeJobPost post = new StripeJobPost();
                    String text = CreateJobPost.this.text.getText().toString();
                    String title = CreateJobPost.this.title.getText().toString();
                    long standardPost = radioGroup.getCheckedRadioButtonId();
                    if (standardPost == cdd_but_id) standardPost = Values.POSTS.CDD_JOB_POST;
                    if (standardPost == cdi_but_id) standardPost = Values.POSTS.CDI_JOB_POST;
                    if (standardPost == extra_but_id) standardPost = Values.POSTS.EXTRA_JOB_POST;
                    String currency = CreateJobPost.this.currency.getSelectedItem().toString();
                    String profession = proffesion.getSelectedItem().toString();
                    String category = CreateJobPost.this.category.getSelectedItem().toString();
                    Integer price = null;

                    if (title.isEmpty()) {
                        CreateJobPost.this.title.setError("Title empty");
                        return;
                    }
                    if (text.isEmpty()) {
                        CreateJobPost.this.text.setError("Text empty");
                        return;
                    }
                    if (currency.isEmpty()) {
                        Toast.makeText(context, "Currency is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (profession.isEmpty()) {
                        Toast.makeText(context, "Profession is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (category.isEmpty()) {
                        Toast.makeText(context, "Category is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (standardPost == Values.POSTS.EXTRA_JOB_POST) {
                        price = Integer.valueOf(CreateJobPost.this.price.getText().toString());
                        if (price == null) {
                            CreateJobPost.this.price.setError("Price is empty");
                            return;
                        }
                    }

                    post.setText(text);
                    post.setTitle(title);
                    post.setType(standardPost);
                    post.setUserid(mAuth.getCurrentUser().getUid());
                    post.setTags(tagsAdapter.getTags());
                    post.setCurrency(currency);
                    post.setPrice(price);
                    post.setProfession(profession);
                    post.setCategory(category);
                    post.setVideourl(videoURL);
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
                    retriever.setDataSource(CreateJobPost.this, videoURI);
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
                                            Toast.makeText(CreateJobPost.this, "Success", Toast.LENGTH_SHORT).show();
                                            videoURL = task.getResult().getMetadata().getDownloadUrl().toString();
                                        }
                                        else{
                                            Toast.makeText(CreateJobPost.this, "Failed", Toast.LENGTH_SHORT).show();
                                            videoURL = null;
                                        }
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        create.setActivated(false);
                                        Toast.makeText(CreateJobPost.this, "Loading video: "+((int)progress)+"%", Toast.LENGTH_SHORT).show();
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
