package com.rayaan.blogpost.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rayaan.blogpost.repository.BlogpostRepository;
import com.rayaan.blogpost.resource.model.BlogRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("blogs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BlogResources {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    BlogpostRepository blogpostRepository;
    public BlogResources(BlogpostRepository blogpostRepository){
        this.blogpostRepository = blogpostRepository;
    }
    @POST
    public Response createBlog(BlogRepresentation blogRepresentation){
        Boolean created = blogpostRepository.addBlog(blogRepresentation);
        if (created){
            return Response.status(Response.Status.CREATED).build();
        }else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    public Response getAll(){
        return Response.ok(blogpostRepository.getAll()).build();
    }

    @GET
    @Path("{blog_id}")
    public Response getBlog(@PathParam("blog_id") String blogId) throws JsonProcessingException {
        final BlogRepresentation blogRepresentation = blogpostRepository.getBlog(blogId);
        if (blogRepresentation == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(OBJECT_MAPPER.writeValueAsString(blogRepresentation)).build();
    }

    @DELETE
    @Path("{blog_id}")
    public Response deleteBlog(@PathParam("blog_id") String blogId){
        if(blogpostRepository.deleteBlog(blogId)){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("update")
    public Response updateBlog(BlogRepresentation blogRepresentation){
        if(blogpostRepository.updateBlog(blogRepresentation)){
            return Response.status(Response.Status.CREATED).build();
        }else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
