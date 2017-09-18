package com.repandco.repco.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post {

    @SerializedName("likes")
    private Long likes;
    @SerializedName("tags")
    private Map<String, Boolean> tags = new HashMap<>();
    @SerializedName("photos")
    private ArrayList<String> photos = new ArrayList<>();
    @SerializedName("text")
    private String text;
    @SerializedName("title")
    private String title;
    @SerializedName("userid")
    private String userid;
    @SerializedName("postID")
    private String postid;
    @SerializedName("date")
    private Long date;
    @SerializedName("type")
    private Long type;

    @SerializedName("likes")
    public Long getLikes() {
        return likes;
    }

    @SerializedName("likes")
    public void setLikes(Long likes) {
        this.likes = likes;
    }

    @SerializedName("tags")
    public Map<String, Boolean> getTags() {
        return tags;
    }

    @SerializedName("tags")
    public void setTags(Map<String, Boolean> tags) {
        this.tags = tags;
    }

    @SerializedName("text")
    public String getText() {
        return text;
    }

    @SerializedName("text")
    public void setText(String text) {
        this.text = text;
    }

    @SerializedName("title")
    public String getTitle() {
        return title;
    }

    @SerializedName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("userid")
    public String getUserid() {
        return userid;
    }

    @SerializedName("userid")
    public void setUserid(String userid) {
        this.userid = userid;
    }

    @SerializedName("date")
    public Long getDate() {
        return date;
    }
    @SerializedName("date")
    public void setDate(Long date) {
        this.date = date;
    }

    @SerializedName("postid")
    public String getPostid() {
        return postid;
    }

    @SerializedName("postid")
    public void setPostid(String postid) {
        this.postid = postid;
    }

    @SerializedName("photos")
    public ArrayList<String> getPhotos() {
        return photos;
    }

    @SerializedName("photos")
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @SerializedName("type")
    public Long getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(Long type) {
        this.type = type;
    }

}