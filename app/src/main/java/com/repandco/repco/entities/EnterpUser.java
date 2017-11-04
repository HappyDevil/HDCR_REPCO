package com.repandco.repco.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EnterpUser {

    @SerializedName("bact")
    private String bact;
    @SerializedName("name")
    private String name;
    @SerializedName("SIRET")
    private String sIRET;
    @SerializedName("address")
    private String address;
    @SerializedName("email")
    private String email;
    @SerializedName("phonenumber")
    private String phonenumber;
    @SerializedName("headerurl")
    private String headerurl;
    @SerializedName("photourl")
    private String photourl;
    @SerializedName("type")
    private Integer type;
    @SerializedName("visible")
    private Integer visible;
    @SerializedName("photos")
    private ArrayList<String> photos;

    @SerializedName("Bact")
    public String getBact() {
        return bact;
    }

    @SerializedName("Bact")
    public void setBact(String bact) {
        this.bact = bact;
    }

    @SerializedName("Bname")
    public String getName() {
        return name;
    }

    @SerializedName("Bname")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("SIRET")
    public String getSIRET() {
        return sIRET;
    }

    @SerializedName("SIRET")
    public void setSIRET(String sIRET) {
        this.sIRET = sIRET;
    }

    @SerializedName("address")
    public String getAddress() {
        return address;
    }

    @SerializedName("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("email")
    public String getEmail() {
        return email;
    }

    @SerializedName("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("phonenumber")
    public String getPhonenumber() {
        return phonenumber;
    }

    @SerializedName("phonenumber")
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @SerializedName("headerurl")
    public String getHeaderurl() {
        return headerurl;
    }

    @SerializedName("headerurl")
    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    @SerializedName("type")
    public Integer getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(Integer type) {
        this.type = type;
    }

    @SerializedName("visible")
    public Integer getVisible() { return visible; }

    @SerializedName("visible")
    public void setVisible(Integer visible) { this.visible = visible; }

    @SerializedName("photos")
    public ArrayList<String> getPhotos() {
        return photos;
    }

    @SerializedName("photos")
    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }
}
