package com.repandco.repco.adapter;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.repandco.repco.constants.Values.REQUEST.LOAD_POST_PHOTO;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageHolder> {

    private ArrayList<String> mDataset;
    private ManagerActivity manager;
    private ImageView plus;
    private boolean addPlus = false;
    private int reqCode;

    public static class ImageHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        private String url;
        private Dialog builder;

        public ImageHolder(CardView v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.imageviewcard);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!url.equals("PLUS")) showImage(url, mImageView);
                    else {
//                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setType("image/*");
//                        manager.startActivityForResult(intent, REQUEST_PHOTO);
                    }
                }
            });
        }

        private void showImage(String url, ImageView mImageView) {
            if (url != null) {
                Context context = mImageView.getContext();

                final Dialog progressDialog = new Dialog(context, R.style.Theme_AppCompat);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.darkTransp)));
                ProgressBar dialogProgrBar = new ProgressBar(context);
                dialogProgrBar.setLayoutParams(new RelativeLayout.LayoutParams(250, 250));
                progressDialog.addContentView(dialogProgrBar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                progressDialog.show();

                builder = new Dialog(context, R.style.Theme_AppCompat);
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

                if (progressDialog.isShowing()) {
                    Picasso.with(context)
                            .load(url)
                            .into(newImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (progressDialog.isShowing()) {
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public ImagesAdapter(ArrayList<String> mDataset,ManagerActivity manager) {
        this.mDataset = mDataset;
        this.manager = manager;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_imagecard, parent, false);
        ImageHolder vh = new ImageHolder((CardView) v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        String url = mDataset.get(position);
        holder.setUrl(url);
        if (url.equals("PLUS")) {
            Picasso.with(holder.mImageView.getContext())
                    .load(R.drawable.ic_plus_24)
                    .resize(10, 10)
                    .into(holder.mImageView);
            plus = holder.mImageView;
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manager.startLoadPhoto(plus,LOAD_POST_PHOTO);
                }
            });
        } else Picasso.with(holder.mImageView.getContext())
                .load(url)
                .resize(200, 200)
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addPhoto(String url) {
        mDataset.add(url);
        notifyDataSetChanged();
    }

    public void addPlus() {
        if (plus == null) {
            mDataset.add("PLUS");
            notifyDataSetChanged();
        }
    }
}


