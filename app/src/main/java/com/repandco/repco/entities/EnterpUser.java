package com.repandco.repco.entities;

import com.google.gson.annotations.SerializedName;
import com.repandco.repco.constants.Keys;

import java.util.ArrayList;

public class EnterpUser {

    @SerializedName(Keys.BACT)
    private String bact;
    @SerializedName(Keys.NAME)
    private String name;
    @SerializedName(Keys.SIRET)
    private String sIRET;
    @SerializedName(Keys.ADDRESS)
    private String address;
    @SerializedName(Keys.EMAIL)
    private String email;
    @SerializedName(Keys.PHONE)
    private String phonenumber;
    @SerializedName(Keys.HEADER)
    private String headerurl;
    @SerializedName(Keys.PHOTO)
    private String photourl;
    @SerializedName(Keys.TYPE)
    private Integer type;
    @SerializedName(Keys.VISIBILITY)
    private Integer visible;
    @SerializedName(Keys.PHOTOS)
    private ArrayList<String> photos;
    @SerializedName(Keys.VIDEO)
    private String videourl;

    @SerializedName(Keys.BACT)
    public String getBact() {
        return bact;
    }

    @SerializedName(Keys.BACT)
    public void setBact(String bact) {
        this.bact = bact;
    }

    @SerializedName(Keys.NAME)
    public String getName() {
        return name;
    }

    @SerializedName(Keys.NAME)
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName(Keys.SIRET)
    public String getSIRET() {
        return sIRET;
    }

    @SerializedName(Keys.SIRET)
    public void setSIRET(String sIRET) {
        this.sIRET = sIRET;
    }

    @SerializedName(Keys.ADDRESS)
    public String getAddress() {
        return address;
    }

    @SerializedName(Keys.ADDRESS)
    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName(Keys.EMAIL)
    public String getEmail() {
        return email;
    }

    @SerializedName(Keys.EMAIL)
    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName(Keys.PHONE)
    public String getPhonenumber() {
        return phonenumber;
    }

    @SerializedName(Keys.PHONE)
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @SerializedName(Keys.HEADER)
    public String getHeaderurl() {
        return headerurl;
    }

    @SerializedName(Keys.HEADER)
    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
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

    @SerializedName(Keys.VISIBILITY)
    public Integer getVisible() { return visible; }

    @SerializedName(Keys.VISIBILITY)
    public void setVisible(Integer visible) { this.visible = visible; }

    @SerializedName(Keys.PHOTOS)
    public ArrayList<String> getPhotos() {
        return photos;
    }

    @SerializedName(Keys.PHOTOS)
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @SerializedName(Keys.VIDEO)
    public String getVideourl() {
        return videourl;
    }

    @SerializedName(Keys.VIDEO)
    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }
}
