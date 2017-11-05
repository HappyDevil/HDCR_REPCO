package com.repandco.repco;

import android.app.Application;

import com.repandco.repco.handlers.VideoRequestHandler;
import com.squareup.picasso.Picasso;

/**
 * Created by Даниил on 01.08.2017.
 */

public class RepCoApp extends Application {

    public Picasso picassoInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        VideoRequestHandler videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new Picasso.Builder(getApplicationContext())
                .addRequestHandler(videoRequestHandler)
                .build();
    }

}
