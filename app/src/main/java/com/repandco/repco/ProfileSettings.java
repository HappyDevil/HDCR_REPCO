package com.repandco.repco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.EnterpUser;
import com.repandco.repco.entities.ProfUser;
import com.squareup.picasso.Picasso;


import java.util.Calendar;
import java.util.Date;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;
import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_HEADER;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_PHOTO;

public class ProfileSettings extends AppCompatActivity {
    private ManagerActivity manager;
    private Toolbar postTolbar;
    private Context context;

    private EditText name;
    private EditText bact;
    private EditText firstname;
    private EditText phonenumber;
    private EditText siret;

    private TextView birthdaySTR;

    private Switch sex;
    private Switch visibility;

    private ImageView photo;
    private ImageView header;

    private ImageButton exitButton;

    private ProfUser profUser;
    private EnterpUser enterpUser;
    private int type;

    private Long birthdayLONG;

    private Button save;

    private LinearLayout birthday;

    private Uri headerURI,photoURI;
    private ProgressDialog progressDialog;


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


        name = (EditText) findViewById(R.id.name);
        bact = (EditText) findViewById(R.id.bact);
        firstname = (EditText) findViewById(R.id.firstname);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        siret = (EditText) findViewById(R.id.siret);

        birthdaySTR = (TextView) findViewById(R.id.birthdaySTR);

        sex = (Switch) findViewById(R.id.sex);
        visibility = (Switch) findViewById(R.id.visibility);

        photo = (ImageView) findViewById(R.id.photo);
        header = (ImageView) findViewById(R.id.header);

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
        if(type == Values.TYPES.PROFESSIONAL_TYPE){
            profUser = new ProfUser();

            profUser.setType(setIntent.getIntExtra(Keys.TYPE,0));
            profUser.setBirthday(setIntent.getLongExtra(Keys.BIRTHDAY,0));
            profUser.setEmail(setIntent.getStringExtra(Keys.EMAIL));
            profUser.setFirstname(setIntent.getStringExtra(Keys.FIRSTNAME));
            profUser.setGender(setIntent.getIntExtra(Keys.GENDER,0));
            profUser.setHeaderurl(setIntent.getStringExtra(Keys.HEADER));
            profUser.setName(setIntent.getStringExtra(Keys.NAME));
            profUser.setPhonenumber(setIntent.getStringExtra(Keys.PHONE));
            profUser.setPhotourl(setIntent.getStringExtra(Keys.PHOTO));
            profUser.setVisible(setIntent.getIntExtra(Keys.VISIBILITY,0));

            birthdayLONG = profUser.getBirthday();


            name.setText(profUser.getName());
            firstname.setText(profUser.getFirstname());
            phonenumber.setText(profUser.getPhonenumber());

            if(profUser.getGender() == Values.GENDERS.MALE) sex.setChecked(false);
            else sex.setChecked(true);

            if(profUser.getVisible() == Values.Visible.PRIVATE) visibility.setChecked(false);
            else visibility.setChecked(true);

            if(profUser.getPhotourl()!=null){
                Picasso.with(context)
                        .load(profUser.getPhotourl())
                        .into(photo);
            }

            if(profUser.getHeaderurl()!=null){
                Picasso.with(context)
                        .load(profUser.getHeaderurl())
                        .into(header);
            }
        }
        else{

            bact.setVisibility(View.VISIBLE);
            siret.setVisibility(View.VISIBLE);
            firstname.setVisibility(View.GONE);
            birthday.setVisibility(View.GONE);
            sex.setVisibility(View.GONE);


            enterpUser = new EnterpUser();

            enterpUser.setType(setIntent.getIntExtra(Keys.TYPE,0));
            enterpUser.setBact(setIntent.getStringExtra(Keys.BACT));
            enterpUser.setEmail(setIntent.getStringExtra(Keys.EMAIL));
            enterpUser.setSIRET(setIntent.getStringExtra(Keys.SIRET));
            enterpUser.setAddress(setIntent.getStringExtra(Keys.ADDRESS));
            enterpUser.setHeaderurl(setIntent.getStringExtra(Keys.HEADER));
            enterpUser.setName(setIntent.getStringExtra(Keys.NAME));
            enterpUser.setPhonenumber(setIntent.getStringExtra(Keys.PHONE));
            enterpUser.setPhotourl(setIntent.getStringExtra(Keys.PHOTO));
            enterpUser.setVisible(setIntent.getIntExtra(Keys.VISIBILITY,0));


            name.setText(enterpUser.getName());
            siret.setText(enterpUser.getSIRET());
            bact.setText(enterpUser.getBact());
            phonenumber.setText(enterpUser.getPhonenumber());

            if(enterpUser.getVisible() == Values.Visible.PRIVATE) visibility.setChecked(false);
            else visibility.setChecked(true);

            if(enterpUser.getPhotourl()!=null){
                Picasso.with(context)
                        .load(enterpUser.getPhotourl())
                        .into(photo);
            }

            if(enterpUser.getHeaderurl()!=null){
                Picasso.with(context)
                        .load(enterpUser.getHeaderurl())
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        }
    }


    private void attemptLogin() {
        // Reset errors.
        Task<Void> databaseTask;
        final Task<UploadTask.TaskSnapshot> photoTask;
        final Task<UploadTask.TaskSnapshot> headerTask;
        final String[] resPhoto = new String[1];
        final String[] resHeader = new String[1];

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("ProgressDialog");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
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

            profUser.setName(name.getText().toString());
            profUser.setFirstname(firstname.getText().toString());
            profUser.setPhonenumber(phonenumber.getText().toString());
            profUser.setBirthday(birthdayLONG);

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


            if(visibility.isChecked()) enterpUser.setVisible(Values.Visible.PRIVATE);
            else enterpUser.setVisible(Values.Visible.PUBLIC);
        }


        if(photoURI != null) photoTask = mStorage.getReference(IMAGES).child(photoURI.getLastPathSegment()).putFile(photoURI)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Loading header");
                            resPhoto[0] = task.getResult().getMetadata().getDownloadUrl().toString();
                        }
                        else {
                            progressDialog.setMessage("Failed load photo");
                            resPhoto[0] =  Values.URLS.STANDARD;
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
            resPhoto[0] = Values.URLS.STANDARD;
        }


        if(headerURI != null) headerTask = mStorage.getReference(IMAGES).child(headerURI.getLastPathSegment()).putFile(headerURI)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.setMessage("Loading header");
                            resHeader[0] = task.getResult().getMetadata().getDownloadUrl().toString();
                        }
                        else {
                            progressDialog.setMessage("Failed load header");
                            resHeader[0] = Values.URLS.STANDARD;
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
            resHeader[0] = Values.URLS.STANDARD;
        }

        switch(type)
        {
            case Values.TYPES.PROFESSIONAL_TYPE:
                photoTask.getResult();
                headerTask.getResult();
                profUser.setHeaderurl(resHeader[0]);
                profUser.setPhotourl(resPhoto[0]);

                progressDialog.setMessage("Loading main info");
                databaseTask = mDatabase.getReference().child(URLS.USERS + mAuth.getCurrentUser().getUid()).setValue(profUser);
                break;
            case Values.TYPES.ENTERPRISE_TYPE:
                photoTask.getResult();
                headerTask.getResult();
                enterpUser.setHeaderurl(resHeader[0]);
                enterpUser.setPhotourl(resPhoto[0]);

                databaseTask = mDatabase.getReference().child("users/"+ mAuth.getCurrentUser().getUid()).setValue(enterpUser);
                break;
            default:
                databaseTask = null;
        }

        if(databaseTask!=null) {
            databaseTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.setMessage("Update successful");
                        progressDialog.dismiss();
//                        Intent goProfile = new Intent(context,ManagerActivity.class);
//                        goProfile.putExtra(Keys.UID,mAuth.getCurrentUser().getUid());
//                        startActivity(goProfile);
                        finish();
                    }
                }
            });
        }
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

                birthdaySTR.setText(cal.toString());
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        newFragment.show();
    }
}