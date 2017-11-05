package com.repandco.repco.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import com.repandco.repco.constants.Keys;

import java.security.Key;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProfUser {

    @SerializedName(Keys.BIRTHDAY)
    private Long birthday;
    @SerializedName(Keys.EMAIL)
    private String email;
    @SerializedName(Keys.FIRSTNAME)
    private String firstname;
    @SerializedName(Keys.GENDER)
    private Integer gender;
    @SerializedName(Keys.NAME)
    private String name;
    @SerializedName(Keys.PHONE)
    private String phonenumber;
    @SerializedName(Keys.PHOTO)
    private String photourl;
    @SerializedName(Keys.HEADER)
    private String headerurl;
    @SerializedName(Keys.TYPE)
    private Integer type;
    @SerializedName(Keys.VISIBILITY)
    private Integer visible;
    @SerializedName(Keys.PHOTOS)
    ArrayList<String> photos;
    @SerializedName(Keys.JOB)
    private String job;
    @SerializedName(Keys.VIDEO)
    private String videourl;
    @SerializedName(Keys.JOB_DESCRIPTION)
    private String jobdescription;

    @SerializedName(Keys.BIRTHDAY)
    public Long getBirthday() {
        return birthday;
    }

    @SerializedName(Keys.BIRTHDAY)
    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    @SerializedName(Keys.EMAIL)
    public String getEmail() {
        return email;
    }

    @SerializedName(Keys.EMAIL)
    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName(Keys.FIRSTNAME)
    public String getFirstname() {
        return firstname;
    }

    @SerializedName(Keys.FIRSTNAME)
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @SerializedName(Keys.GENDER)
    public Integer getGender() {
        return gender;
    }

    @SerializedName(Keys.GENDER)
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @SerializedName(Keys.NAME)
    public String getName() {
        return name;
    }

    @SerializedName(Keys.NAME)
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName(Keys.PHONE)
    public String getPhonenumber() {
        return phonenumber;
    }

    @SerializedName(Keys.PHONE)
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @SerializedName(Keys.PHOTO)
    public String getPhotourl() {
        return photourl;
    }

    @SerializedName(Keys.PHOTO)
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    @SerializedName(Keys.TYPE)
    public Integer getType() {
        return type;
    }

    @SerializedName(Keys.TYPE)
    public void setType(Integer type) {
        this.type = type;
    }

    @SerializedName(Keys.HEADER)
    public String getHeaderurl() {
        return headerurl;
    }

    @SerializedName(Keys.HEADER)
    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
    }

    @SerializedName(Keys.VISIBILITY)
    public Integer getVisible() {
        return visible;
    }

    @SerializedName(Keys.VISIBILITY)
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    @SerializedName(Keys.PHOTOS)
    public ArrayList<String> getPhotos() {
        return photos;
    }

    @SerializedName(Keys.PHOTOS)
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @SerializedName(Keys.JOB)
    public String getJob() {
        return job;
    }

    @SerializedName(Keys.JOB)
    public void setJob(String job) {
        this.job = job;
    }

    @SerializedName(Keys.VIDEO)
    public String getVideourl() {
        return videourl;
    }

    @SerializedName(Keys.VIDEO)
    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    @SerializedName(Keys.JOB_DESCRIPTION)
    public String getJobdescription() {
        return jobdescription;
    }

    @SerializedName(Keys.JOB_DESCRIPTION)
    public void setJobdescription(String jobdescription) {
        this.jobdescription = jobdescription;
    }
}
