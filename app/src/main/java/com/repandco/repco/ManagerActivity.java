package com.repandco.repco;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.repandco.repco.constants.Toasts;
import com.repandco.repco.constants.Values;
import com.repandco.repco.customClasses.LoadPhotoAct;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.entities.EnterpUser;
import com.repandco.repco.entities.ProfUser;
import com.repandco.repco.entities.StripeJobPost;
import com.repandco.repco.mainFragments.ChoseType;
import com.repandco.repco.mainFragments.NotifFragment;
import com.repandco.repco.mainFragments.PostFragment;
import com.repandco.repco.mainFragments.ProfileFragment;
import com.repandco.repco.mainFragments.SearchFragment;
import com.repandco.repco.request.CloudFuncAPI;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;
import static com.repandco.repco.constants.URLS.USERS;

public class ManagerActivity extends LoadPhotoAct implements  BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fTrans;

    private Retrofit cloudFunctions;
    public CloudFuncAPI cloudAPI;

    public Picasso picassoInstance;
    public long type;
    private String status;
    private String stripeEmail;
    private String email;
    private DatabaseReference reference;

    private AlertDialog alertDialog;
    private ProgressBar progress;
    private Dialog progressDialog;

    private Boolean open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        picassoInstance = ((RepCoApp)getApplication()).picassoInstance;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);

        progressDialog = new Dialog(this, R.style.Theme_AppCompat);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darkTransp)));
        ProgressBar dialogProgrBar = new ProgressBar(this);
        dialogProgrBar.setLayoutParams(new RelativeLayout.LayoutParams(250, 250));
        progressDialog.addContentView(dialogProgrBar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        cloudFunctions = new Retrofit.Builder()
                .baseUrl(URLS.cloudFunc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cloudAPI = cloudFunctions.create(CloudFuncAPI.class);

        Toolbar t = (Toolbar) findViewById(R.id.transtoolbar);
        setSupportActionBar(t);

        fTrans = getFragmentManager().beginTransaction();

        reference = mDatabase.getReference();
        updateAccountInfo();

        reference.child(URLS.USERS+mAuth.getCurrentUser().getUid()).child(Keys.STATUS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status = dataSnapshot.getValue(String.class);
                status = (status==null) ? Values.STATUS.ERROR : status;
                if(open){
                    progress.setVisibility(View.GONE);
                    alertDialog.cancel();
                    open = false;
                }
                if(status.equals(Values.STATUS.ACTIVE)){
                    if(type==Values.TYPES.ENTERPRISE_TYPE) Toast.makeText(ManagerActivity.this, "Account active", Toast.LENGTH_SHORT).show();
                }
                onCreateDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String tag = getIntent().getStringExtra(Keys.TAG);
        String uid = getIntent().getStringExtra(Keys.UID);
        uid = (uid==null) ? mAuth.getCurrentUser().getUid() : uid;
        if (tag != null) {
            openSearh(tag);
        }
        else {
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setManager(this);

            Bundle bundle = new Bundle();
            bundle.putString(Keys.UID, uid);
            profileFragment.setArguments(bundle);

            fTrans.add(R.id.frgmCont, profileFragment);
            fTrans.commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == R.id.navigation_add_post) && (!onCreateDialog())) return false;

        if (!item.isChecked()) {
            fTrans = getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    onCreateDialog();
                    PostFragment postFragment = new PostFragment();
                    postFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, postFragment);
                    break;
                case R.id.navigation_dashboard:
                    onCreateDialog();
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, searchFragment);
                    break;
                case R.id.navigation_add_post:
                    ChoseType choseType = new ChoseType();
                    choseType.setManager(this);
                    fTrans.replace(R.id.frgmCont, choseType);
                    break;
                case R.id.navigation_notifications:
                    onCreateDialog();
                    NotifFragment notifFragment = new NotifFragment();
                    notifFragment.setManager(this);
                    fTrans.replace(R.id.frgmCont, notifFragment);
                    break;
                case R.id.navigation_profile:
                    onCreateDialog();
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
        onCreateDialog();
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
        onCreateDialog();
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


    public void showProgress(){
        progressDialog.show();
    }

    public void hideProgress(){
        if (progressDialog.isShowing()) progressDialog.hide();
    }

    public void showImage(String url, ImageView mImageView){
            if (url != null) {
                Context context = mImageView.getContext();


                showProgress();

                final Dialog builder = new Dialog(context, R.style.Theme_AppCompat);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.darkTransp)));
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

                Picasso.with(context)
                        .load(url)
                        .into(newImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                    hideProgress();
                                    builder.show();
                            }

                            @Override
                            public void onError() {

                            }
                        });

                builder.addContentView(newImage, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public void openPost(StripeJobPost model) {
        onCreateDialog();
        Intent postIntent = new Intent(this, OpenPost.class);
        postIntent.putExtra(Keys.TEXT, model.getText());
        postIntent.putExtra(Keys.COMMISSION, model.getCommission());
        postIntent.putExtra(Keys.CURRENCY, model.getCurrency());
        postIntent.putExtra(Keys.PRICE, model.getPrice());
        postIntent.putExtra(Keys.CATEGORY, model.getCategory());
        postIntent.putExtra(Keys.DATE, model.getDate());
        postIntent.putExtra(Keys.LIKES, model.getLikes());
        postIntent.putExtra(Keys.PHOTOS, model.getPhotos());
        postIntent.putExtra(Keys.POSTID, model.getPostid());
        postIntent.putExtra(Keys.PROFESSION, model.getProfession());
        postIntent.putExtra(Keys.TITLE, model.getTitle());
        postIntent.putExtra(Keys.TYPE, model.getType());
        postIntent.putExtra(Keys.UID, model.getUserid());
        postIntent.putExtra(Keys.TEXT, model.getText());
        String[] tags = new String[model.getTags().keySet().size()];
        model.getTags().keySet().toArray(tags);
        postIntent.putExtra(Keys.TAGS, tags);
        startActivity(postIntent);
    }

    public void openSettings(int type,Object user){
        onCreateDialog();
        if(user==null) return;
        if(type == Values.TYPES.PROFESSIONAL_TYPE){
            ProfUser profUser = (ProfUser) user;

            Intent setIntent = new Intent(this, ProfileSettings.class);

            setIntent.putExtra(Keys.TYPE,profUser.getType());
            setIntent.putExtra(Keys.BIRTHDAY,profUser.getBirthday());
            setIntent.putExtra(Keys.EMAIL,profUser.getEmail());
            setIntent.putExtra(Keys.FIRSTNAME,profUser.getFirstname());
            setIntent.putExtra(Keys.GENDER,profUser.getGender());
            setIntent.putExtra(Keys.HEADER,profUser.getHeaderurl());
            setIntent.putExtra(Keys.NAME,profUser.getName());
            setIntent.putExtra(Keys.PHONE,profUser.getPhonenumber());
            setIntent.putExtra(Keys.PHOTO,profUser.getPhotourl());
            setIntent.putExtra(Keys.PHOTOS,profUser.getPhotos());
            setIntent.putExtra(Keys.VISIBILITY,profUser.getVisible());
            setIntent.putExtra(Keys.JOB_DESCRIPTION,profUser.getJob());
            setIntent.putExtra(Keys.JOB,profUser.getJobdescription());

            startActivity(setIntent);
        }
        else {
            EnterpUser enterpUser = (EnterpUser) user;

            Intent setIntent = new Intent(this, ProfileSettings.class);

            setIntent.putExtra(Keys.TYPE,enterpUser.getType());
            setIntent.putExtra(Keys.BACT,enterpUser.getBact());
            setIntent.putExtra(Keys.EMAIL,enterpUser.getEmail());
            setIntent.putExtra(Keys.SIRET,enterpUser.getSIRET());
            setIntent.putExtra(Keys.ADDRESS,enterpUser.getAddress());
            setIntent.putExtra(Keys.HEADER,enterpUser.getHeaderurl());
            setIntent.putExtra(Keys.NAME,enterpUser.getName());
            setIntent.putExtra(Keys.PHONE,enterpUser.getPhonenumber());
            setIntent.putExtra(Keys.PHOTO,enterpUser.getPhotourl());
            setIntent.putExtra(Keys.VISIBILITY,enterpUser.getVisible());

            startActivity(setIntent);
        }
    }

    public void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, FirstActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    public boolean onCreateDialog() {
        if(((status==null)||(status.equals(Values.STATUS.ERROR)))&&(type==Values.TYPES.ENTERPRISE_TYPE)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();


            View inflate = inflater.inflate(R.layout.alert_dialog_stripe, null);
            builder.setView(inflate);

            Button button = (Button) inflate.findViewById(R.id.button7);
            final EditText editText = (EditText) inflate.findViewById(R.id.editText2);
            progress = (ProgressBar) inflate.findViewById(R.id.progress);

            if(stripeEmail!=null) editText.setText(stripeEmail);
            else if(email!=null) editText.setText(email);

            alertDialog = builder.create();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.setVisibility(View.VISIBLE);
                    String newEmail = editText.getText().toString();
                    if(newEmail.isEmpty()){
                        editText.setError("Email is empty");
                        progress.setVisibility(View.GONE);
                        return;
                    }
                    if((!newEmail.contains("@"))||(newEmail.length()<4)){
                        editText.setError("Email is wrong");
                        progress.setVisibility(View.GONE);
                        return;
                    }
                    open = true;
                    if((stripeEmail==null)||(!stripeEmail.equals(newEmail))) {
                        stripeEmail = newEmail;
                        reference.child(URLS.USERS + mAuth.getCurrentUser().getUid()).child(Keys.STRIPE_EMAIL).setValue(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (open) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress.setVisibility(View.GONE);
                                                    alertDialog.cancel();
                                                    Toast.makeText(ManagerActivity.this, "Error create plan!", Toast.LENGTH_SHORT).show();
                                                    open = false;
                                                }
                                            });
                                        }
                                    }
                                }, 10000L);
                            }
                        });
                    }
                    else {
                        cloudAPI.createSubscription(mAuth.getCurrentUser().getUid()).enqueue(new retrofit2.Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                progress.setVisibility(View.GONE);
                                alertDialog.cancel();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                }
            });
            alertDialog.show();
            return false;
        }
        return true;
    }

    private void updateAccountInfo(){
        reference.child(USERS + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    type = (long) dataSnapshot.child(Keys.TYPE).getValue();
                    status = dataSnapshot.child(Keys.STATUS).getValue(String.class);
                    stripeEmail = dataSnapshot.child(Keys.STRIPE_EMAIL).getValue(String.class);
                    email = dataSnapshot.child(Keys.EMAIL).getValue(String.class);
                    onCreateDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManagerActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProfile(String uid){
        onCreateDialog();
        fTrans = getFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setManager(this);

        Bundle bundle = new Bundle();
        bundle.putString(Keys.UID, uid);
        profileFragment.setArguments(bundle);

        fTrans.replace(R.id.frgmCont, profileFragment);
        fTrans.commit();

    }

    public void openPost(final String postID) {
        mDatabase.getReference().child(URLS.POSTS+postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StripeJobPost post = dataSnapshot.getValue(StripeJobPost.class);
                if(post!=null) {
                    openPost(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ManagerActivity.this, "Internet error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openVideo(String videourl,Context context) {
        if (videourl != null) {
            showProgress();

            final Dialog builder = new Dialog(context, R.style.Theme_AppCompat);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.darkTransp)));
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    hideProgress();
                }
            });
            VideoView videoView = new VideoView(context);
            videoView.setVisibility(View.VISIBLE);

            MediaController mc = new MediaController(context);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            Uri video = Uri.parse(videourl);
            videoView.setMediaController(mc);
            videoView.setVideoURI(video);
            videoView.start();

            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.hide();
                    hideProgress();
                }
            });

            builder.addContentView(videoView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
