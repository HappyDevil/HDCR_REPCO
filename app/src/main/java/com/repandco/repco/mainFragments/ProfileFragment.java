package com.repandco.repco.mainFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.repandco.repco.FirebaseConfig;
import com.repandco.repco.FirstActivity;
import com.repandco.repco.Followers_Act;
import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.repandco.repco.adapter.ImagesAdapter;
import com.repandco.repco.adapter.PostAdapter;
import com.repandco.repco.constants.Keys;
import com.repandco.repco.constants.URLS;
import com.repandco.repco.constants.Values;
import com.repandco.repco.entities.EnterpUser;
import com.repandco.repco.entities.ProfUser;
import com.repandco.repco.handlers.VideoRequestHandler;
import com.repandco.repco.listeners.PostListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.repandco.repco.FirebaseConfig.mAuth;
import static com.repandco.repco.FirebaseConfig.mDatabase;
import static com.repandco.repco.constants.Toasts.ERR_INTERNET;
import static com.repandco.repco.constants.URLS.ChildURLS.FOLLOW;
import static com.repandco.repco.constants.URLS.ChildURLS.FOLLOWED;
import static com.repandco.repco.constants.URLS.FRIENDS;
import static com.repandco.repco.constants.URLS.RATINGS;
import static com.repandco.repco.constants.URLS.USERS;

public class ProfileFragment extends Fragment {

    private ManagerActivity manager;
    private TextView usename;
    private TextView rateTimes;
    private TextView noimages;
    private TextView novideo;
    private TextView friendCount;
    private TextView nopost;
    private RatingBar ratingBar;
    private Button follow;
    private Button rate;
    private LinearLayout portfolio;
    private CardView card_gender;
    private CardView card_birthday;
    private CardView card_email;
    private CardView card_phone;
    private CardView card_bact;
    private CardView card_job;
    private CardView card_job_description;
    private CardView friends;
    private CardView card_address;
    private RecyclerView history;
    private RecyclerView.LayoutManager historyLayoutManager;
    private PostAdapter postAdapter;
    private RoundedImageView photo;
    private ImageView header;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton exitButton;

    private ImageView videoView;

    private EnterpUser enterpUser;
    private ProfUser profUser;


    private String photourl;
    private double ratingV;
    private double curRate;
    private long rateTimesV;
    private DatabaseReference reference;
    private View context;
    private String uid;
    private String curUserID;
    private ProgressBar bar;
    private long friendType = -1;
    private View content;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        reference = FirebaseConfig.mDatabase.getReference();

        uid = this.getArguments().getString(Keys.UID);
        context = inflater.inflate(R.layout.fragment_profile, container,false);


        if(uid!=null) {
            if(context!=null) {
                content = context;
                bar = (ProgressBar) context.findViewById(R.id.temp);
                header = (ImageView) context.findViewById(R.id.header);
                if (content != null){
                    bar.setProgress(0);
                    bar.setMax(3);
                    bar.setVisibility(View.VISIBLE);
                    content.setVisibility(View.INVISIBLE);
                    usename = (TextView) content.findViewById(R.id.username);
                    noimages = (TextView) content.findViewById(R.id.noimages);
                    novideo = (TextView) content.findViewById(R.id.novideo);
                    friendCount = (TextView) content.findViewById(R.id.friendCount);
                    mRecyclerView = (RecyclerView) content.findViewById(R.id.my_recycler_view);
                    history = (RecyclerView) content.findViewById(R.id.history);
                    videoView = (ImageView) content.findViewById(R.id.videoView);

                    rateTimes = (TextView) content.findViewById(R.id.rateTimes);
                    ratingBar = (RatingBar) content.findViewById(R.id.ratingBar);
                    follow = (Button) content.findViewById(R.id.follow);
                    rate = (Button) content.findViewById(R.id.rate);
                    portfolio = (LinearLayout) content.findViewById(R.id.portfolio);
                    photo = (RoundedImageView) content.findViewById(R.id.photo);

                    card_gender = (CardView) content.findViewById(R.id.card_gender);
                    card_birthday = (CardView) content.findViewById(R.id.card_birthday);
                    card_email = (CardView) content.findViewById(R.id.card_email);
                    card_phone = (CardView) content.findViewById(R.id.card_phone);
                    card_bact = (CardView) content.findViewById(R.id.card_bact);
                    card_job = (CardView) content.findViewById(R.id.card_job);
                    card_job_description = (CardView) content.findViewById(R.id.card_job_description);
                    card_address = (CardView) content.findViewById(R.id.card_address);
                    friends = (CardView) content.findViewById(R.id.friends);

                    friends.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent followIntent = new Intent(manager, Followers_Act.class);
                            followIntent.putExtra(Keys.UID,uid);
                            startActivity(followIntent);
                        }
                    });
                    exitButton = (FloatingActionButton) content.findViewById(R.id.exitButton);

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(context.getContext(),LinearLayoutManager.HORIZONTAL,false);
                    mLayoutManager.offsetChildrenHorizontal(10);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    history.setHasFixedSize(false);
                    historyLayoutManager = new LinearLayoutManager(context.getContext(),LinearLayoutManager.VERTICAL,false);
                    historyLayoutManager.offsetChildrenHorizontal(15);
                    historyLayoutManager.setAutoMeasureEnabled(true);
                    history.setLayoutManager(historyLayoutManager);

                    postAdapter = new PostAdapter(manager);

                    NestedScrollView nsv = (NestedScrollView) content.findViewById(R.id.nested_profile);
                    nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY > oldScrollY) {
                                if(curUserID.equals(uid)) exitButton.hide();
                            } else {
                                if(curUserID.equals(uid)) exitButton.show();
                            }
                        }
                    });
                    nopost = (TextView) content.findViewById(R.id.nopost);
                    mDatabase.getReference().child(URLS.POSTS).orderByChild(Keys.USERID).equalTo(uid).addListenerForSingleValueEvent(new PostListener(postAdapter,nopost));


                    history.setAdapter(postAdapter);

                    curUserID = mAuth.getCurrentUser().getUid();
                    if(curUserID.equals(this.uid)) {
                        follow.setVisibility(View.GONE);
                        rate.setVisibility(View.GONE);
                        exitButton.setVisibility(View.VISIBLE);
                        exitButton.show();
                        exitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(enterpUser!=null){
                                    manager.openSettings(enterpUser.getType(),enterpUser);
                                }
                                if(profUser!=null){
                                    manager.openSettings(profUser.getType(),profUser);
                                }
                            }
                        });
                        if(manager!=null) manager.getBottomNavigationView().getMenu().findItem(R.id.navigation_profile).setChecked(true);
                    }
                    else {
                        follow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if((friendType == -1)||(friendType==2)) {
                                    follow.setText(R.string.followed);
                                    if(friendType == -1){
                                        friendType = 0;
                                        follow.setText(R.string.followed);
                                    }
                                    else {
                                        friendType = 1;
                                        follow.setText(R.string.firends);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        follow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context.getContext(), R.color.cardtags2)));
                                    }
                                    reference.child(URLS.FRIENDS+curUserID).child(FOLLOW).child(uid).setValue(true);
                                }
                                else {
                                    follow.setText(R.string.follow);
                                    friendType = -1;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        follow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context.getContext(), R.color.addjobpost)));
                                    }
                                    reference.child(URLS.FRIENDS+curUserID).child(FOLLOW).child(uid).removeValue();
                                }
                            }
                        });
                        rate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showRatingDialog();
                            }
                        });

                        reference.child(RATINGS+ this.uid +"/"+ curUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null) {
                                    Object rateObj= dataSnapshot.child(Keys.RATING).getValue();
                                    if(rateObj!=null) {
                                        if (rateObj instanceof Double) curRate = (double) rateObj;
                                        else curRate = (Long) rateObj;
                                    }
                                }
                                bar.setProgress(1);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    reference.child(FRIENDS+uid).child(FOLLOWED).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                            int i = 0;
                            while(iterator.hasNext()) {
                              i++;
                              iterator.next();
                            }

                            friendCount.setText(String.valueOf(i));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            friendCount.setText(String.valueOf(0));
                        }
                    });
                }
                if (usename != null)
                {
                    reference.child(FRIENDS+curUserID).child(FOLLOW).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null){
                                friendType = (long) dataSnapshot.child(Keys.TYPE).getValue();
                                if(friendType == 0) follow.setText(R.string.followed);
                                else follow.setText(R.string.firends);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    follow.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context.getContext(), R.color.cardtags2)));
                                }
                            }
                            else {
                                reference.child(FRIENDS + curUserID).child(FOLLOWED).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()!=null) {
                                            friendType = 2;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            bar.setProgress(2);
                            reference.child(USERS + uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    long type = (long) dataSnapshot.child(Keys.TYPE).getValue();

                                    if (type == Values.TYPES.PROFESSIONAL_TYPE) profUser = dataSnapshot.getValue(ProfUser.class);
                                    else enterpUser = dataSnapshot.getValue(EnterpUser.class);

                                    Object rateObj = dataSnapshot.child(Keys.RATING).getValue();
                                    if(rateObj!=null) {
                                        if (rateObj instanceof Double) ratingV = (double) rateObj;
                                        else ratingV = (Long) rateObj;
                                        ratingBar.setRating((float) ratingV);
                                    }


                                    photourl = (String) dataSnapshot.child(Keys.PHOTO).getValue();
                                    String headerurl = (String) dataSnapshot.child(Keys.HEADER).getValue();
                                     final String videourl = (String) dataSnapshot.child(Keys.VIDEO).getValue();
                                    ArrayList<String> photos = (ArrayList<String>) dataSnapshot.child(Keys.PHOTOS).getValue();
                                    Object rateTimesObj = dataSnapshot.child(Keys.RATETIMES).getValue();
                                    if(rateTimesObj!=null) {
                                        rateTimesV = (long) rateTimesObj;
                                        rateTimes.setText("Rated " + rateTimesV + " times");
                                    }
                                    else rateTimes.setText("Rated " + 0 + " times");
                                    if(videourl!=null){
                                        novideo.setVisibility(View.GONE);
                                        videoView.setVisibility(View.VISIBLE);
                                        videoView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                manager.openVideo(videourl,context.getContext());
                                            }
                                        });
                                    }
                                    else {
                                        novideo.setVisibility(View.VISIBLE);
                                        videoView.setVisibility(View.GONE);
                                    }
                                    if(photos!=null) {
                                        noimages.setVisibility(View.GONE);
                                        mAdapter = new ImagesAdapter(photos,manager,null);
                                        mRecyclerView.setAdapter(mAdapter);
                                    }

                                    if(headerurl!=null) {
                                        if (!headerurl.equals(Values.URLS.STANDARD)) {
                                            Picasso.with(context.getContext())
                                                    .load(headerurl)
                                                    .resize(header.getWidth(),header.getHeight())
                                                    .centerCrop()
                                                    .into(header);
                                        }
                                    }
                                    if(photourl!=null) {
                                        if (!photourl.equals(Values.URLS.STANDARD)) {
                                            Picasso.with(context.getContext())
                                                    .load(photourl)
                                                    .resize(photo.getWidth(),photo.getHeight())
                                                    .centerCrop()
                                                    .into(photo);

                                            photo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    manager.showImage(photourl,photo);
                                                }
                                            });
                                        }
                                    }
                                    ((TextView) card_email.findViewById(R.id.text_email)).setText(dataSnapshot.child(Keys.EMAIL).getValue(String.class));
                                    ((TextView) card_phone.findViewById(R.id.text_phone)).setText(dataSnapshot.child(Keys.PHONE).getValue(String.class));


                                    String value = dataSnapshot.child(Keys.STATUS).getValue(String.class);

                                    if (type == Values.TYPES.PROFESSIONAL_TYPE) {
                                        enterpriseInfo(View.GONE);
                                        proffesionalInfo(View.VISIBLE);

                                        String profName = dataSnapshot.child(Keys.NAME).getValue(String.class);
                                        profName = upperCaseFirstLetter(profName);
                                        String profFirstName = dataSnapshot.child(Keys.FIRSTNAME).getValue(String.class);
                                        profFirstName = upperCaseFirstLetter(profFirstName);
                                        String name = profName + " " + profFirstName;
                                        usename.setText(name);

                                        Integer gender = dataSnapshot.child(Keys.GENDER).getValue(Integer.class);
                                        if (gender != null) {
                                            if (gender == Values.GENDERS.FEMALE)
                                                ((TextView) card_gender.findViewById(R.id.text_gender)).setText(R.string.female);
                                        }
                                        Long birthday = dataSnapshot.child(Keys.BIRTHDAY).getValue(Long.class);
                                        if (birthday != null)
                                            ((TextView) card_birthday.findViewById(R.id.text_birthday)).setText(DateFormat.getDateInstance().format(new Date(birthday)));

                                        ((TextView) card_job.findViewById(R.id.text_job)).setText(dataSnapshot.child(Keys.JOB).getValue(String.class));
                                        ((TextView) card_job_description.findViewById(R.id.text_job_description)).setText(dataSnapshot.child(Keys.JOB_DESCRIPTION).getValue(String.class));

                                    } else {
                                        if (type == Values.TYPES.ENTERPRISE_TYPE) {
                                            proffesionalInfo(View.GONE);
                                            enterpriseInfo(View.VISIBLE);

                                            String name = dataSnapshot.child("name").getValue(String.class);
                                            name = upperCaseFirstLetter(name);
                                            usename.setText(name);

                                            ((TextView) card_bact.findViewById(R.id.text_bact)).setText(dataSnapshot.child(Keys.BACT).getValue(String.class));
                                            ((TextView) card_address.findViewById(R.id.text_address)).setText(dataSnapshot.child(Keys.ADDRESS).getValue(String.class));
                                        }
                                    }

                                    mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            bar.setVisibility(View.INVISIBLE);
                                            content.setVisibility(View.VISIBLE);
                                            content.scrollTo(0,0);
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(context.getContext(), ERR_INTERNET, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    usename.setText(uid);
                }
            }
        }
        return context;
    }

    public ManagerActivity getManager() {
        return manager;
    }

    public void setManager(ManagerActivity manager) {
        this.manager = manager;
    }

    private void proffesionalInfo(int visibility){
        card_gender.setVisibility(visibility);
        card_birthday.setVisibility(visibility);
        card_job.setVisibility(visibility);
        card_job_description.setVisibility(visibility);
    }

    private void enterpriseInfo(int visibility){
        card_bact.setVisibility(visibility);
        card_address.setVisibility(visibility);
    }

    private void contacInfo(int visibility){
        card_email.setVisibility(visibility);
        card_phone.setVisibility(visibility);
    }

    public void showRatingDialog() {
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(context.getContext());

        LayoutInflater inflater = manager.getLayoutInflater();


        View inflate = inflater.inflate(R.layout.rating, null);
        ratingdialog.setView(inflate);


        final RatingBar rating = (RatingBar) inflate.findViewById(R.id.ratingBar2);

        ratingdialog.setIcon(android.R.drawable.btn_star_big_on);
        if(curRate!=0) ratingdialog.setTitle("Your last voute: "+curRate);
        else ratingdialog.setTitle("Choose rate:");

        ratingdialog.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        curRate = rating.getRating();
                        manager.cloudAPI.rateReq(uid,curUserID, curRate).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(context.getContext(),"Rated",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        ratingdialog.create();
        ratingdialog.show();
    }

    private String upperCaseFirstLetter(String name){
        if(name!=null) {
            String s1 = name.substring(0, 1).toUpperCase();
            name = s1 + name.substring(1);
        }
        else name = "";
        return name;
    }

}

