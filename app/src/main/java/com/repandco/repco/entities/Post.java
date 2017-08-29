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
        "imageurl",
        "job",
        "likes",
        "tags",
        "text",
        "title",
        "userid"
})
public class Post {

    @JsonProperty("imageurl")
    private String imageurl;
    @JsonProperty("job")
    private Boolean job;
    @JsonProperty("likes")
    private Map<String, Boolean> likes;
    @JsonProperty("tags")
    private Map<String, Boolean> tags = new HashMap<>();
    @JsonProperty("text")
    private String text;
    @JsonProperty("title")
    private String title;
    @JsonProperty("userid")
    private String userid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("imageurl")
    public String getImageurl() {
        return imageurl;
    }

    @JsonProperty("imageurl")
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @JsonProperty("job")
    public Boolean getJob() {
        return job;
    }

    @JsonProperty("job")
    public void setJob(Boolean job) {
        this.job = job;
    }

    @JsonProperty("likes")
    public  Map<String, Boolean> getLikes() {
        return likes;
    }

    @JsonProperty("likes")
    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    @JsonProperty("tags")
    public Map<String, Boolean> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(Map<String, Boolean> tags) {
        this.tags = tags;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("userid")
    public String getUserid() {
        return userid;
    }

    @JsonProperty("userid")
    public void setUserid(String userid) {
        this.userid = userid;
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