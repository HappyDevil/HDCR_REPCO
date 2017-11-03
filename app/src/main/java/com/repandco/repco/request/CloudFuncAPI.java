package com.repandco.repco.request;

import com.repandco.repco.constants.URLS;
import com.repandco.repco.entities.StripeJobPost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface CloudFuncAPI {

    @GET(URLS.rateReq)
    Call<Void> rateReq(@Query("to") String to, @Query("from") String from,@Query("rate") Long rate);

    @GET(URLS.developNotReq)
    Call<Void> developNotReq(@Query("work") String work, @Query("dev") String dev);

    @GET(URLS.findFriendsPost)
    Call<StripeJobPost[]> findFriendsPost(@Query("id") String id, @Query("chunk") Long chunk);

    @GET(URLS.createSubscription)
    Call<Void> createSubscription(@Query("id") String id);
}
