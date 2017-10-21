package com.repandco.repco.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.repandco.repco.customClasses.ImageViewLoader;
import com.repandco.repco.customClasses.LoadPhotoAct;
import com.repandco.repco.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.repandco.repco.FirebaseConfig.mStorage;
import static com.repandco.repco.constants.URLS.IMAGES;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageHolder> {

    private ArrayList<String> mDataset;
    private LoadPhotoAct loadPhoto;
    public ImageViewLoader plus;
    private ImagesAdapter imagesAdapter;

    public static class ImageHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public ProgressBar photoprogress;
        private String url;
        private Dialog builder;
        private CardView v;

        public ImageHolder(CardView v) {
            super(v);
            this.v = v;
            photoprogress = (ProgressBar) v.findViewById(R.id.photoprogress);
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

        public void setUrl(final String url) {
            this.url = url;

            if (!url.equals("PLUS")) mImageView = (ImageView) v.findViewById(R.id.imageviewcard);
            else mImageView = (ImageView) v.findViewById(R.id.imageviewcard);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!url.equals("PLUS")) showImage(url, mImageView);
                }
            });
        }
    }

    public ImagesAdapter(ArrayList<String> mDataset,LoadPhotoAct loadPhoto) {
        this.mDataset = mDataset;
        this.loadPhoto = loadPhoto;
        this.imagesAdapter = this;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_imagecard, parent, false);
        ImageHolder vh = new ImageHolder((CardView) v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, final int position) {
        String url = mDataset.get(position);
        holder.setUrl(url);
        if (!url.equals("PLUS"))
            Picasso.with(holder.mImageView.getContext())
                    .load(url)
                    .resize(300,300)
                    .centerCrop()
                    .into(holder.mImageView);
        else {
            plus = new ImageViewLoader(holder.mImageView);
            plus.setProgressBar(holder.photoprogress);
            plus.getImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        if (imageView.getTag() != null) {
                            String s = imageView.getTag().toString();
                            mStorage.getReference(IMAGES).child(s).delete();
                            imageView.setTag(null);
                            imageView.setImageResource(R.drawable.ic_plus_24);
                            mDataset.remove(s);
                            notifyItemChanged(position);
                        } else {
                            loadPhoto.loadPhoto(imagesAdapter);
                            plus.getProgressBar().setVisibility(View.VISIBLE);
                            notifyItemChanged(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addPhoto(String url) {
        mDataset.add(url);
        notifyDataSetChanged();
    }

    public void deletePhoto(String url) {
        mDataset.remove(url);
        notifyDataSetChanged();
    }

    public void convertUrlPhoto(String fromURL,String toURL){
        for(int i=0;i<mDataset.size();i++){
            if(mDataset.get(i).equals(fromURL)){
                mDataset.set(i,toURL);
                return;
            }
        }
    }

    public void convertPlus(String url){
        convertUrlPhoto("PLUS",url);
    }


    public void addPlus() {
        if(!mDataset.contains("PLUS"))
            mDataset.add("PLUS");
            notifyItemChanged(mDataset.size());
    }

    public ArrayList<String> getPhotos() {
        return mDataset;
    }
}


