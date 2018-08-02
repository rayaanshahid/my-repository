package com.rayaan.blogpost.repository;

import com.rayaan.blogpost.resource.model.BlogRepresentation;

import java.util.List;

public interface BlogpostRepository {
    @Deprecated
    Boolean addBlog(BlogRepresentation blogRepresentation);
    BlogRepresentation getBlog(String blogId);
    List<BlogRepresentation> getAll();
    Boolean deleteBlog(String blogId);
    Boolean updateBlog(BlogRepresentation blogRepresentation);
}
