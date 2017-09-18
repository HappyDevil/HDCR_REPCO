package com.repandco.repco.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ProfUser {

    @SerializedName("birthday")
    private Long birthday;
    @SerializedName("email")
    private String email;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("name")
    private String name;
    @SerializedName("phonenumber")
    private String phonenumber;
    @SerializedName("photourl")
    private String photourl;
    @SerializedName("headerurl")
    private String headerurl;
    @SerializedName("type")
    private Integer type;
    @SerializedName("visible")
    private Integer visible;

    @SerializedName("birthday")
    public Long getBirthday() {
        return birthday;
    }

    @SerializedName("birthday")
    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    @SerializedName("email")
    public String getEmail() {
        return email;
    }

    @SerializedName("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("firstname")
    public String getFirstname() {
        return firstname;
    }

    @SerializedName("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @SerializedName("gender")
    public Integer getGender() {
        return gender;
    }

    @SerializedName("gender")
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }

    @SerializedName("name")
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("phonenumber")
    public String getPhonenumber() {
        return phonenumber;
    }

    @SerializedName("phonenumber")
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @SerializedName("photourl")
    public String getPhotourl() {
        return photourl;
    }

    @SerializedName("photourl")
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

    public String getHeaderurl() {
        return headerurl;
    }

    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
    }

    @SerializedName("visible")

    public Integer getVisible() {
        return visible;
    }

    @SerializedName("visible")
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

}
