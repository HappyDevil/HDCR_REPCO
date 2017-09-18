package com.repandco.repco.entities;

import com.google.gson.annotations.SerializedName;


public class JobPost extends Post {
    @SerializedName("category")
    private String category;
    @SerializedName("developerID")
    private String developerID;
    @SerializedName("profession")
    private String profession;

    @SerializedName("category")
    public String getCategory() {
        return category;
    }

    @SerializedName("category")
    public void setCategory(String category) {
        this.category = category;
    }


    @SerializedName("profession")
    public String getProfession() {
        return profession;
    }

    @SerializedName("profession")
    public void setProfession(String profession) {
        this.profession = profession;
    }

    @SerializedName("developerID")
    public String getDeveloperID() {
        return developerID;
    }

    @SerializedName("developerID")
    public void setDeveloperID(String developerID) {
        this.developerID = developerID;
    }
}
