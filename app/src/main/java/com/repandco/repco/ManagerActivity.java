package com.repandco.repco;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
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
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, searchFragment);
                    break;
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
}
