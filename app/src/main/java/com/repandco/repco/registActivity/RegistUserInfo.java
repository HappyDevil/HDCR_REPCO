package com.repandco.repco.registActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.repandco.repco.FirstActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;
import com.squareup.picasso.Picasso;

import static com.repandco.repco.constants.Values.REQUEST.REQUEST_HEADER;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_PHOTO;

public class RegistUserInfo extends AppCompatActivity {

    private Intent intent;
    private EditText name;
    private EditText firstname;
    private EditText email;
    private ImageButton photobut;
    private ImageButton headerbut;
    private Toolbar postTolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_user_info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Register auth info:");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        intent = getIntent();

        name = (EditText) findViewById(R.id.name);
        firstname = (EditText) findViewById(R.id.firsname);
        email = (EditText) findViewById(R.id.emailaddress);
        photobut = (ImageButton) findViewById(R.id.photobut);
        headerbut = (ImageButton) findViewById(R.id.headerbut);
        context = this;

    }

    public void next(View view) {
        if(intent!=null)
        {
            String nameStr = name.getText().toString();
            String firstnameStr = firstname.getText().toString();
            String emailStr = email.getText().toString();

            if(TextUtils.isEmpty(nameStr)){
                name.setError("Name is empty!");
                name.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(firstnameStr)){
                firstname.setError("Firstname is empty!");
                firstname.requestFocus();
                return;
            }

            if(TextUtils.isEmpty(emailStr)){
                email.setError("Email is empty!");
                email.requestFocus();
                return;
            }
            else {
                if(!emailStr.contains("@")) {
                    email.setError("Email is wrong!");
                    email.requestFocus();
                    return;
                }
            }

            intent.putExtra(Keys.NAME, nameStr);
            intent.putExtra(Keys.FIRSTNAME, firstnameStr);
            intent.putExtra(Keys.EMAIL, emailStr);
            intent.setClass(this,RegistPersonalInfo.class);
        }
        else intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

    public void header(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_HEADER);
    }


    public void photo(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();

                if (requestCode == REQUEST_HEADER){
                    Picasso.with(context)
                            .load(uri)
                            .resize(300,300)
                            .into(headerbut);
//                    headerbut.setImageURI(uri);
                    intent.putExtra(Keys.HEADER, String.valueOf(uri));
                }
                else
                if (requestCode == REQUEST_PHOTO){
//                    photobut.setImageURI(uri);
                    Picasso.with(context)
                            .load(uri)
                            .resize(300,300)
                            .into(photobut);
                    intent.putExtra(Keys.PHOTO, String.valueOf(uri));
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
