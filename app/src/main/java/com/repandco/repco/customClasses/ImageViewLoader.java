package com.repandco.repco.customClasses;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Даниил on 10.10.2017.
 */

public class ImageViewLoader extends android.support.v7.widget.AppCompatImageView {

    private ProgressBar progressBar;
    private String loadPhotoUrl;
    private ImageView imageView;

    public ImageViewLoader(Context context) {
        super(context);
    }

    public ImageViewLoader(ImageView imageView){
        super(imageView.getContext());
//        super.= imageView;
        this.imageView = imageView;

    }

    public String getLoadPhotoUrl() {
        return loadPhotoUrl;
    }

    public void setLoadPhotoUrl(String loadPhotoUrl) {
        this.loadPhotoUrl = loadPhotoUrl;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
