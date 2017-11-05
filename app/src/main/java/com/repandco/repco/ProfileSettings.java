package com.repandco.repco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.customClasses.LoadPhotoAct;
import com.repandco.repco.entities.EnterpUser;
import com.repandco.repco.entities.ProfUser;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;
import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;
import static com.repandco.repco.constants.URLS.VIDEOS;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_HEADER;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_PHOTO;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_VIDEO;

public class ProfileSettings extends LoadPhotoAct {
    private ManagerActivity manager;
    private Toolbar postTolbar;
    private Context context;

    private EditText name;
    private EditText bact;
    private EditText firstname;
    private EditText phonenumber;
    private EditText job;
    private EditText text_job_description;
    private EditText siret;

    private TextView birthdaySTR;

    private Switch sex;
    private Switch visibility;

    private ImageView photo;
    private ImageView header;
    private ImageView videoView;

    private ImageButton exitButton;

    private ProfUser profUser;
    private EnterpUser enterpUser;
    private int type;

    private Long birthdayLONG;

    private Button save;

    private LinearLayout birthday;

    private Uri headerURI,photoURI;
    private ProgressDialog progressDialog;

    private String header_START_STR,photo_START_STR;
    private RecyclerView photos;
    private ImagesAdapter imagesAdapter;;
    private Uri videoURI;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (ManagerActivity) getParent();
        context =this;
        setContentView(R.layout.activity_settings_profile_simple);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        photos = (RecyclerView) findViewById(R.id.photos);
        photos.setHasFixedSize(false);
        RecyclerView.LayoutManager  photosLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        photos.setLayoutManager(photosLayoutManager);

        name = (EditText) findViewById(R.id.name);
        bact = (EditText) findViewById(R.id.bact);
        firstname = (EditText) findViewById(R.id.firstname);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        job = (EditText) findViewById(R.id.job);
        text_job_description = (EditText) findViewById(R.id.text_job_description);
        siret = (EditText) findViewById(R.id.siret);

        birthdaySTR = (TextView) findViewById(R.id.birthdaySTR);

        sex = (Switch) findViewById(R.id.sex);
        visibility = (Switch) findViewById(R.id.visibility);

        photo = (ImageView) findViewById(R.id.photo);
        header = (ImageView) findViewById(R.id.header);
        videoView = (ImageView) findViewById(R.id.videoView);

        exitButton = (ImageButton) findViewById(R.id.exitButton);

        save = (Button) findViewById(R.id.save);

        birthday = (LinearLayout) findViewById(R.id.birthday);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(context, FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                finish();
            }
        });

        Intent setIntent = getIntent();

        type = setIntent.getIntExtra(Keys.TYPE,0);
        header_START_STR = setIntent.getStringExtra(Keys.HEADER);
        photo_START_STR = setIntent.getStringExtra(Keys.PHOTO);
        if(type == Values.TYPES.PROFESSIONAL_TYPE){
            profUser = new ProfUser();

            profUser.setType(setIntent.getIntExtra(Keys.TYPE,0));
            profUser.setBirthday(setIntent.getLongExtra(Keys.BIRTHDAY,0));
            profUser.setEmail(setIntent.getStringExtra(Keys.EMAIL));
            profUser.setFirstname(setIntent.getStringExtra(Keys.FIRSTNAME));
            profUser.setGender(setIntent.getIntExtra(Keys.GENDER,0));
            profUser.setHeaderurl(header_START_STR);
            profUser.setName(setIntent.getStringExtra(Keys.NAME));
            profUser.setPhonenumber(setIntent.getStringExtra(Keys.PHONE));
            profUser.setPhotourl(photo_START_STR);
            profUser.setVisible(setIntent.getIntExtra(Keys.VISIBILITY,0));
            profUser.setPhotos(setIntent.getStringArrayListExtra(Keys.PHOTOS));
            profUser.setJob(setIntent.getStringExtra(Keys.JOB));
            profUser.setJobdescription(setIntent.getStringExtra(Keys.JOB_DESCRIPTION));

            imagesAdapter = new ImagesAdapter(profUser.getPhotos(),this,true,save);
            imagesAdapter.addPlus();
            photos.setAdapter(imagesAdapter);

            birthdayLONG = profUser.getBirthday();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(birthdayLONG));

            birthdaySTR.setText(DateFormat.getDateInstance().format(calendar.getTime()));


            name.setText(profUser.getName());
            firstname.setText(profUser.getFirstname());
            phonenumber.setText(profUser.getPhonenumber());
            job.setText(profUser.getJob());
            text_job_description.setText(profUser.getJobdescription());

            if(profUser.getGender() == Values.GENDERS.MALE){
                sex.setChecked(false);
            }
            else{
                sex.setChecked(true);
            }

            sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String text = "Male";
                    if(b) text = "Female";
                    sex.setText(text);
                }
            });

            if(profUser.getVisible() == Values.Visible.PRIVATE) visibility.setChecked(false);
            else visibility.setChecked(true);

            visibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String text = "Private";
                    if(b) text = "Public";
                    visibility.setText(text);
                }
            });

            if(profUser.getPhotourl()!=null){
                Picasso.with(context)
                        .load(profUser.getPhotourl())
                        .resize(450,450)
                        .centerCrop()
                        .into(photo);
            }

            if(profUser.getHeaderurl()!=null){
                Picasso.with(context)
                        .load(profUser.getHeaderurl())
                        .resize(450,450)
                        .centerCrop()
                        .into(header);
            }
        }
        else{

            bact.setVisibility(View.VISIBLE);
            siret.setVisibility(View.VISIBLE);
            firstname.setVisibility(View.GONE);
            job.setVisibility(View.GONE);
            text_job_description.setVisibility(View.GONE);
            birthday.setVisibility(View.GONE);
            sex.setVisibility(View.GONE);


            enterpUser = new EnterpUser();

            enterpUser.setType(setIntent.getIntExtra(Keys.TYPE,0));
            enterpUser.setBact(setIntent.getStringExtra(Keys.BACT));
            enterpUser.setEmail(setIntent.getStringExtra(Keys.EMAIL));
            enterpUser.setSIRET(setIntent.getStringExtra(Keys.SIRET));
            enterpUser.setAddress(setIntent.getStringExtra(Keys.ADDRESS));
            enterpUser.setHeaderurl(header_START_STR);
            enterpUser.setName(setIntent.getStringExtra(Keys.NAME));
            enterpUser.setPhonenumber(setIntent.getStringExtra(Keys.PHONE));
            enterpUser.setPhotourl(photo_START_STR);
            enterpUser.setPhotos(setIntent.getStringArrayListExtra(Keys.PHOTOS));
            enterpUser.setVisible(setIntent.getIntExtra(Keys.VISIBILITY,0));

            imagesAdapter = new ImagesAdapter(enterpUser.getPhotos(),this,save);
            imagesAdapter.addPlus();
            photos.setAdapter(imagesAdapter);

            name.setText(enterpUser.getName());
            siret.setText(enterpUser.getSIRET());
            bact.setText(enterpUser.getBact());
            phonenumber.setText(enterpUser.getPhonenumber());

            if(enterpUser.getVisible() == Values.Visible.PRIVATE) visibility.setChecked(false);
            else visibility.setChecked(true);

            if(enterpUser.getPhotourl()!=null){
                Picasso.with(context)
                        .load(enterpUser.getPhotourl())
                        .resize(450,450)
                        .centerCrop()
                        .into(photo);
            }

            if(enterpUser.getHeaderurl()!=null){
                Picasso.with(context)
                        .load(enterpUser.getHeaderurl())
                        .resize(450,450)
                        .centerCrop()
                        .into(header);
            }
        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_HEADER);
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_VIDEO);
            }
        });
        save.setActivated(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(save.isActivated()) attemptLogin();
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();
                if (requestCode == REQUEST_HEADER){
                    header.setImageURI(uri);
                    headerURI = uri;
                }
                else
                if (requestCode == REQUEST_PHOTO){
                    photo.setImageURI(uri);
                    photoURI = uri;
                }
                if (requestCode == REQUEST_VIDEO){
                    videoView.setBackgroundColor(getResources().getColor(R.color.cardtags2));
                    videoURI = uri;
                    if(videoURI!=null) {
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        //use one of overloaded setDataSource() functions to set your data source
                        retriever.setDataSource(context, videoURI);
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long timeInMillisec = Long.parseLong(time);
                        retriever.release();
                        if (timeInMillisec > 60000){
                            videoView.setBackgroundColor(getResources().getColor(R.color.addjobpost));
                            videoURI = null;
                            Toast.makeText(manager, "Error, File is too long", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    Task<Void> databaseTask;
    boolean h = false;
    boolean p = false;
    boolean v = false;
    private void attemptLogin() {
        // Reset errors.

        final Task<UploadTask.TaskSnapshot> photoTask;
        final Task<UploadTask.TaskSnapshot> headerTask;

        final String[] resPhoto = new String[1];
        final String[] resVideo = new String[1];
        final String[] resHeader = new String[1];

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("ProgressDialog");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Update account info");

        name.setError(null);
        phonenumber.setError(null);
        if(name.length()==0){
            name.setError("Name is empty!");
            name.requestFocus();
            return;
        }
        if(phonenumber.length()==0){
            phonenumber.setError("Phonenumber is empty!");
            phonenumber.requestFocus();
            return;
        }


        if(type == Values.TYPES.PROFESSIONAL_TYPE){
            firstname.setError(null);
            if(firstname.length()==0){
                firstname.setError("Firstname is empty!");
                firstname.requestFocus();
                return;
            }
            job.setError(null);
            if(job.length()==0){
                job.setError("Job is empty");
                job.requestFocus();
                return;
            }
            if(text_job_description.length()==0){
                text_job_description.setError("Job is empty");
                text_job_description.requestFocus();
                return;
            }

            profUser.setName(name.getText().toString());
            profUser.setFirstname(firstname.getText().toString());
            profUser.setPhonenumber(phonenumber.getText().toString());
            profUser.setJob(job.getText().toString());
            profUser.setJobdescription(text_job_description.getText().toString());
            profUser.setBirthday(birthdayLONG);
            profUser.setPhotos(imagesAdapter.getPhotos());

            if(sex.isChecked()) profUser.setGender(Values.GENDERS.FEMALE);
            else profUser.setGender(Values.GENDERS.MALE);

            if(visibility.isChecked()) profUser.setVisible(Values.Visible.PRIVATE);
            else profUser.setVisible(Values.Visible.PUBLIC);


        }
        else{
            bact.setError(null);
            siret.setError(null);
            if(bact.length()==0){
                bact.setError("Activity is empty!");
                bact.requestFocus();
                return;
            }
            if(siret.length()==0){
                siret.setError("SIRET is empty!");
                siret.requestFocus();
                return;
            }

            enterpUser.setBact(bact.getText().toString());
            enterpUser.setSIRET(siret.getText().toString());
            enterpUser.setPhotos(imagesAdapter.getPhotos());

            if(visibility.isChecked()) enterpUser.setVisible(Values.Visible.PRIVATE);
            else enterpUser.setVisible(Values.Visible.PUBLIC);
        }
        imagesAdapter.deletePhoto("PLUS");
        progressDialog.show();
        progressDialog.setMessage("Loading photo");
        if(photoURI != null) photoTask = mStorage.getReference(IMAGES).child(photoURI.getLastPathSegment()).putFile(photoURI)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Loading header");
                            resPhoto[0] = task.getResult().getMetadata().getDownloadUrl().toString();
                            p = true;
                        }
                        else {
                            progressDialog.setMessage("Failed load photo");
                            resPhoto[0] =  (photo_START_STR==null) ? Values.URLS.STANDARD : photo_START_STR;
                            p = true;
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setProgress((int) progress);
                    }
                });
        else {
            photoTask = null;
            p = true;
            resPhoto[0] = (photo_START_STR==null) ? Values.URLS.STANDARD : photo_START_STR;
        }

        progressDialog.setMessage("Loading video");
        if(videoURI!=null) {
                mStorage.getReference(VIDEOS).child(videoURI.getLastPathSegment()).putFile(videoURI)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    resVideo[0] = task.getResult().getMetadata().getDownloadUrl().toString();
                                    v = true;
                                } else {
                                    progressDialog.setMessage("Failed load video");
                                    resVideo[0] = null;
                                    v = true;
                                }
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setProgress((int) progress);
                            }
                        });
        }
        else {
            v = true;
            resVideo[0] = null;
        }

        if(headerURI != null) headerTask = mStorage.getReference(IMAGES).child(headerURI.getLastPathSegment()).putFile(headerURI)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Loading header");
                            resHeader[0] = task.getResult().getMetadata().getDownloadUrl().toString();
                            h = true;
                        }
                        else {
                            progressDialog.setMessage("Failed load header");
                            resHeader[0] = (header_START_STR==null) ? Values.URLS.STANDARD : header_START_STR;
                            h = true;
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setProgress((int) progress);
                    }
                });
        else{
            headerTask = null;
            h = true;
            resHeader[0] = (header_START_STR==null) ? Values.URLS.STANDARD : header_START_STR;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(h&&p&&v) break;
                }

                switch(type)
                {
                    case Values.TYPES.PROFESSIONAL_TYPE:
                        profUser.setHeaderurl(resHeader[0]);
                        profUser.setPhotourl(resPhoto[0]);
                        if(resVideo[0]!=null) profUser.setVideourl(resVideo[0]);
                        databaseTask = mDatabase.getReference().child(URLS.USERS + mAuth.getCurrentUser().getUid()).setValue(profUser);
                        break;
                    case Values.TYPES.ENTERPRISE_TYPE:
                        enterpUser.setHeaderurl(resHeader[0]);
                        enterpUser.setPhotourl(resPhoto[0]);
                        if(resVideo[0]!=null) enterpUser.setVideourl(resVideo[0]);
                        databaseTask = mDatabase.getReference().child("users/"+ mAuth.getCurrentUser().getUid()).setValue(enterpUser);
                        break;
                    default:
                        databaseTask = null;
                }

                if(databaseTask !=null) {
                    databaseTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setMessage("Update successful");
                                        progressDialog.dismiss();
                                    }
                                });
                                Intent goProfile = new Intent(context,ManagerActivity.class);
                                goProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                goProfile.putExtra(Keys.UID,mAuth.getCurrentUser().getUid());
                                startActivity(goProfile);
                                finish();
                                finish();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void showDatePickerDialog(View view) {
        Long birthday = profUser.getBirthday();
        Date brthday = new Date(birthday);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(brthday);


        DatePickerDialog newFragment = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mouth, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, mouth);
                cal.set(Calendar.DAY_OF_MONTH, day);
                birthdayLONG = cal.getTimeInMillis();

                birthdaySTR.setText(DateFormat.getDateInstance().format(cal.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        newFragment.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}