package com.repandco.repco;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.mainActivities.CreateComPost;
import com.repandco.repco.mainActivities.NotifFragment;
import com.repandco.repco.mainActivities.PostFragment;
import com.repandco.repco.mainActivities.ProfileFragment;
import com.repandco.repco.mainActivities.SearchFragment;
import com.repandco.repco.request.CloudFuncAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;
import static com.repandco.repco.constants.Values.REQUEST.LOAD_POST_PHOTO;

public class ManagerActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fTrans;

    private Retrofit cloudFunctions;
    public CloudFuncAPI cloudAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson));

        cloudFunctions = new Retrofit.Builder()
                .baseUrl(URLS.cloudFunc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cloudAPI = cloudFunctions.create(CloudFuncAPI.class);

        Toolbar t = (Toolbar) findViewById(R.id.transtoolbar);
        setSupportActionBar(t);

        fTrans = getFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setManager(this);

        Bundle bundle = new Bundle();
        bundle.putString(Keys.UID, mAuth.getCurrentUser().getUid());
        profileFragment.setArguments(bundle);

        fTrans.add(R.id.frgmCont, profileFragment);
        fTrans.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(!item.isChecked()) {
            fTrans = getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    PostFragment postFragment = new PostFragment();
                    postFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, postFragment);
                    break;
                case R.id.navigation_dashboard:
                    CreateComPost createComPost = new CreateComPost();
                    createComPost.setManager(this);
                    fTrans.replace(R.id.frgmCont, createComPost);
                    break;
//                    SearchFragment searchFragment = new SearchFragment();
//                    searchFragment.setManager(this);
//                    fTrans.replace(R.id.frgmCont, searchFragment);
//                    break;
                case R.id.navigation_notifications:
                    NotifFragment notifFragment = new NotifFragment();
                    notifFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, notifFragment);
                    break;
                case R.id.navigation_profile:
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setManager(this);

                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.UID, mAuth.getCurrentUser().getUid());
                    profileFragment.setArguments(bundle);
                    fTrans.replace(R.id.frgmCont, profileFragment);
                    break;
            }
            fTrans.addToBackStack(null);
            fTrans.commit();
            return true;
        }
        return false;
    }

    public void openProfile(String uid) {
        fTrans = getFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setManager(this);

        Bundle bundle = new Bundle();
        bundle.putString(Keys.UID, uid);
        profileFragment.setArguments(bundle);

        fTrans.replace(R.id.frgmCont, profileFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    public void openSearh(String search) {
        fTrans = getFragmentManager().beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setManager(this);


        Bundle bundle = new Bundle();
        bundle.putString(Keys.SEARCH, search);
        searchFragment.setArguments(bundle);

        fTrans.replace(R.id.frgmCont, searchFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    public void showImage(String url, ImageView mImageView){
        if(url!=null) {
            Context context = mImageView.getContext();

            final Dialog progressDialog = new Dialog(context, R.style.Theme_AppCompat);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context,R.color.darkTransp)));
            ProgressBar dialogProgrBar = new ProgressBar(context);
            dialogProgrBar.setLayoutParams(new RelativeLayout.LayoutParams(250,250));
            progressDialog.addContentView(dialogProgrBar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            progressDialog.show();


            final Dialog builder = new Dialog(context, R.style.Theme_AppCompat);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context,R.color.darkTransp)));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                }
            });
            ImageView newImage = new ImageView(context);

            newImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.hide();
                }
            });

            if(progressDialog.isShowing()) {
                Picasso.with(context)
                        .load(url)
                        .into(newImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                if(progressDialog.isShowing()) {
                                    progressDialog.hide();
                                    builder.show();
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
            builder.addContentView(newImage, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private ImageView imageView;
    public void startLoadPhoto(ImageView imageView,int reqCode){
        this.imageView = imageView;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();

                if (requestCode == LOAD_POST_PHOTO){
                    imageView.setImageURI(uri);
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

}
