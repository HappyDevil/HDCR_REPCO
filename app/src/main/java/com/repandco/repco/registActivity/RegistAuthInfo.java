package com.repandco.repco.registActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.repandco.repco.FirebaseConfig;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.EnterpUser;
import com.repandco.repco.entities.ProfUser;

import java.util.ArrayList;
import java.util.Map;

import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_HEADER;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_PHOTO;

public class RegistAuthInfo extends AppCompatActivity {

    private EditText password;
    private OnCompleteListener register;
    private String email;
    private Intent intent;
    private FirebaseUser mUser;
    private OnCompleteListener<Void> goToProfile;
    private Context context;
    private ProgressDialog progressDialog;
    private OnCompleteListener waitDelete;
    private Toolbar postTolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_auth_info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Register auth info:");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        intent = getIntent();
        
        email = getIntent().getStringExtra(Keys.EMAIL);
        
        password = (EditText) findViewById(R.id.password);

        context = this;

        configProgressDialog();

        register  = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    progressDialog.setMessage("Register successful");
                    mUser = FirebaseConfig.mAuth.getCurrentUser();
                    Task<Void> databaseTask = addUserToFirebase();
                    if(databaseTask!=null) databaseTask.addOnCompleteListener(goToProfile);
                    progressDialog.setMessage("Add personal info...");
                }
                else
                {
                    progressDialog.dismiss();
                    String message = task.getException().getMessage();
                    Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
                }

            }
        };

        waitDelete = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                }
            }
        };

        goToProfile = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressDialog.setMessage("Adding successful");
                    Intent goProfile = new Intent(context,ManagerActivity.class);
                    goProfile.putExtra(Keys.UID,mUser.getUid());
                    progressDialog.dismiss();
                    startActivity(goProfile);
                }
                else mUser.delete().addOnCompleteListener(waitDelete);
            }
        };

    }

    private void configProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("ProgressDialog");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    public void next(View view) {
        String passwordStr = password.getText().toString();
        if(TextUtils.isEmpty(passwordStr)){
            password.setError("Password is empty!");
            password.requestFocus();
            return;
        }
        else {
            if(passwordStr.length()<6){
                password.setError("Password is too short!");
                password.requestFocus();
                return;
            }
        }
        progressDialog.show();
        FirebaseConfig.mAuth.createUserWithEmailAndPassword(email, passwordStr).addOnCompleteListener(this,register);
        progressDialog.setMessage("Register account...");
    }


    private Task<Void> addUserToFirebase()
    {
        int type = intent.getIntExtra(Keys.TYPE, Values.TYPES.DEFAULT_TYPE);
        Task<Void> databaseTask = null;
        final Task<UploadTask.TaskSnapshot> photoTask;
        final Task<UploadTask.TaskSnapshot> headerTask;
        DatabaseReference reference = FirebaseConfig.mDatabase.getReference();

        final String[] resPhoto = new String[1];
        final String[] resHeader = new String[1];

        String photoSTR = intent.getStringExtra(Keys.PHOTO);
        final String headerSTR = intent.getStringExtra(Keys.HEADER);

        final Uri photoURI = (photoSTR!=null) ? Uri.parse(photoSTR) : null;
        final Uri headerURI = (headerSTR!=null) ? Uri.parse(headerSTR) : null;

        progressDialog.setMessage("Loading photo");
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
            case 1:
                final ProfUser profUser = new ProfUser();
                profUser.setName(intent.getStringExtra(Keys.NAME));
                profUser.setEmail(email);
                profUser.setFirstname(intent.getStringExtra(Keys.FIRSTNAME));
                profUser.setBirthday(intent.getLongExtra(Keys.BIRTHDAY,0));
                profUser.setPhonenumber(intent.getStringExtra(Keys.PHONE));
                profUser.setGender(intent.getIntExtra(Keys.GENDER,0));
                profUser.setType(type);
                profUser.setVisible(Values.Visible.PRIVATE);

                photoTask.getResult();
                headerTask.getResult();
                profUser.setHeaderurl(resHeader[0]);
                profUser.setPhotourl(resPhoto[0]);

                progressDialog.setMessage("Loading main info");
                databaseTask = reference.child(URLS.USERS+mUser.getUid()).setValue(profUser);
                break;
            case 2:
                EnterpUser enterpUser = new EnterpUser();

                enterpUser.setPhonenumber(intent.getStringExtra(Keys.PHONE));
                enterpUser.setAddress(intent.getStringExtra(Keys.ADDRESS));
                enterpUser.setBact(intent.getStringExtra(Keys.BACT));
                enterpUser.setName(intent.getStringExtra(Keys.NAME));
                enterpUser.setSIRET(intent.getStringExtra(Keys.SIRET));
                enterpUser.setEmail(email);
                enterpUser.setVisible(Values.Visible.PRIVATE);
                enterpUser.setType(type);

                photoTask.getResult();
                headerTask.getResult();
                enterpUser.setHeaderurl(resHeader[0]);
                enterpUser.setPhotourl(resPhoto[0]);

                databaseTask = reference.child("users/"+mUser.getUid()).setValue(enterpUser);
                break;
        }

        return databaseTask;
    }
}
