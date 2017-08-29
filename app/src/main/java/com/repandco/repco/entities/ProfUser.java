package com.repandco.repco.entities;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "birthday",
        "email",
        "firstname",
        "gender",
        "name",
        "phonenumber",
        "photourl",
        "type",
        "visible"
})

public class ProfUser {

    @JsonProperty("birthday")
    private Long birthday;
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("gender")
    private Integer gender;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phonenumber")
    private String phonenumber;
    @JsonProperty("photourl")
    private String photourl;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("visible")
    private Integer visible;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("birthday")
    public Long getBirthday() {
        return birthday;
    }

    @JsonProperty("birthday")
    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    @JsonProperty("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @JsonProperty("gender")
    public Integer getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("phonenumber")
    public String getPhonenumber() {
        return phonenumber;
    }

    @JsonProperty("phonenumber")
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @JsonProperty("photourl")
    public String getPhotourl() {
        return photourl;
    }

    @JsonProperty("photourl")
    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    @JsonProperty("type")
    public Integer getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(Integer type) {
        this.type = type;
    }

    @JsonProperty("visible")
    public Integer getVisible() {
        return visible;
    }

    @JsonProperty("visible")
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
