package com.rayaan.blogpost.resource.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogRepresentation {
    @JsonProperty(value = "blog_id")
    private String blogId;
    @JsonProperty(value = "created_by")
    private String createdBy;
    @JsonProperty(value = "blog_topic")
    private String blogTopic;
    private String headline;
    @JsonProperty(value = "blog_text")
    private String blogText;
    private String image;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBlogTopic() {
        return blogTopic;
    }

    public void setBlogTopic(String blogTopic) {
        this.blogTopic = blogTopic;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }
}
