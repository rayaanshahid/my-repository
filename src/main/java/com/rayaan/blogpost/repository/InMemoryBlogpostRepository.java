package com.rayaan.blogpost.repository;

import com.rayaan.blogpost.resource.model.BlogRepresentation;

import java.util.List;

public class InMemoryBlogpostRepository implements BlogpostRepository{
    private List<BlogRepresentation> database;

    @Override
    public Boolean addBlog(BlogRepresentation blogRepresentation) {
        return null;
    }

    @Override
    public BlogRepresentation getBlog(String blogId) {
        return null;
    }

    @Override
    public List<BlogRepresentation> getAll() {
        return null;
    }

    @Override
    public Boolean deleteBlog(String blogId) {
        return null;
    }

    @Override
    public Boolean updateBlog(BlogRepresentation blogRepresentation) {
        return null;
    }
}
