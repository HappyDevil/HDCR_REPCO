package com.repandco.repco.registActivity;

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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.repandco.repco.FirstActivity;
import com.repandco.repco.R;
import com.repandco.repco.constants.Keys;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;
import static com.repandco.repco.constants.URLS.USERS;
import static com.repandco.repco.constants.Values.REQUEST.LOAD_POST_PHOTO;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_HEADER;
import static com.repandco.repco.constants.Values.REQUEST.REQUEST_PHOTO;

public class RegistBusinessInfo extends AppCompatActivity {


    private Intent intent;
    private EditText bact;
    private EditText bname;
    private EditText siret;
    private ImageButton photobut;
    private ImageButton headerbut;
    private Toolbar postTolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_business_info);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        postTolbar = (Toolbar) findViewById(R.id.postTolbar);
        postTolbar.setTitle("Register auth info:");
        setSupportActionBar(postTolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        intent = getIntent();

        bact = (EditText) findViewById(R.id.bact);
        bname = (EditText) findViewById(R.id.name);
        siret = (EditText) findViewById(R.id.siret);
        photobut = (ImageButton) findViewById(R.id.photobut);
        headerbut = (ImageButton) findViewById(R.id.headerbut);
    }

    public void next(View view) {
        if(intent!=null)
        {
            String bactStr = bact.getText().toString();
            String bnnameStr = bname.getText().toString();
            String siretStr = siret.getText().toString();

            if(TextUtils.isEmpty(bnnameStr)){
                bname.setError("Name is empty!");
                bname.requestFocus();
                return;
            }
            else {
                if(bnnameStr.length()<2){
                    bname.setError("Name is wrong!");
                    bname.requestFocus();
                    return;
                }
            }

            if(TextUtils.isEmpty(bactStr)){
                bact.setError("Bact is empty!");
                bact.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(siretStr)){
                siret.setError("Siret is empty!");
                siret.requestFocus();
                return;
            }

            intent.putExtra(Keys.BACT, bactStr);
            intent.putExtra(Keys.NAME, bnnameStr);
            intent.putExtra(Keys.SIRET, siretStr);
            intent.setClass(this,RegistBusinessContacts.class);
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
                    headerbut.setImageURI(uri);
                    intent.putExtra(Keys.HEADER, String.valueOf(uri));
                }
                else
                if (requestCode == REQUEST_PHOTO){
                    photobut.setImageURI(uri);
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
