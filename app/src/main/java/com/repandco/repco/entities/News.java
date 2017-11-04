package com.repandco.repco.entities;

/**
 * Created by Даниил on 18.09.2017.
 */

public class News {
    private String uid;
    private Long date;
    private Long type;
    private String postID;
    private StripeJobPost postREF;

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public StripeJobPost getPostREF() {
        return postREF;
    }

    public void setPostREF(StripeJobPost postREF) {
        this.postREF = postREF;
    }
}
