package com.repandco.repco.entities;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Bact",
        "Bname",
        "SIRET",
        "address",
        "email",
        "phonenumber",
        "photourl",
        "type",
        "visible"
})

public class EnterpUser {


    @JsonProperty("Bact")
    private String bact;
    @JsonProperty("Bname")
    private String bname;
    @JsonProperty("SIRET")
    private String sIRET;
    @JsonProperty("address")
    private String address;
    @JsonProperty("email")
    private String email;
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

    @JsonProperty("Bact")
    public String getBact() {
        return bact;
    }

    @JsonProperty("Bact")
    public void setBact(String bact) {
        this.bact = bact;
    }

    @JsonProperty("Bname")
    public String getBname() {
        return bname;
    }

    @JsonProperty("Bname")
    public void setBname(String bname) {
        this.bname = bname;
    }

    @JsonProperty("SIRET")
    public String getSIRET() {
        return sIRET;
    }

    @JsonProperty("SIRET")
    public void setSIRET(String sIRET) {
        this.sIRET = sIRET;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
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
    public Integer getVisible() { return visible; }

    @JsonProperty("visible")
    public void setVisible(Integer visible) { this.visible = visible; }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
