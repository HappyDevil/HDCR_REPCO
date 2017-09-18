package com.repandco.repco.registActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.repandco.repco.FirebaseConfig;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.EnterpUser;
import com.repandco.repco.entities.ProfUser;
import com.repandco.repco.mainActivities.ProfileFragment;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_auth_info);
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
        DatabaseReference reference = FirebaseConfig.mDatabase.getReference();
        switch(type)
        {
            case 1:
                ProfUser profUser = new ProfUser();
                profUser.setName(intent.getStringExtra(Keys.NAME));
                profUser.setEmail(email);
                profUser.setFirstname(intent.getStringExtra(Keys.FIRSTNAME));
                profUser.setBirthday(intent.getLongExtra(Keys.BIRTHDAY,0));
                profUser.setPhonenumber(intent.getStringExtra(Keys.PHONE));
                profUser.setGender(intent.getIntExtra(Keys.GENDER,0));
                profUser.setType(type);
                profUser.setVisible(Values.Visible.PRIVATE);
                if(intent.getStringExtra(Keys.PHOTO)!=null) profUser.setPhotourl(intent.getStringExtra(Keys.PHOTO));
                else profUser.setPhotourl(Values.URLS.STANDARD);
                if(intent.getStringExtra(Keys.HEADER)!=null) profUser.setHeaderurl(intent.getStringExtra(Keys.HEADER));
                else profUser.setHeaderurl(Values.URLS.STANDARD);

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
                if(intent.getStringExtra(Keys.PHOTO)!=null) enterpUser.setPhotourl(Values.URLS.STANDARD);
                else enterpUser.setPhotourl(Values.URLS.STANDARD);
                if(intent.getStringExtra(Keys.HEADER)!=null) enterpUser.setHeaderurl(Values.URLS.STANDARD);
                else enterpUser.setHeaderurl(Values.URLS.STANDARD);

                databaseTask = reference.child("users/"+mUser.getUid()).setValue(enterpUser);
                break;
        }

        return databaseTask;
    }
}
