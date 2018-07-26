package com.rayaan.blogpost.repository.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rayaan.blogpost.AdditionalConfig;
import com.rayaan.blogpost.kafka.event.KafkaBlogpostEventProducer;
import com.rayaan.blogpost.repository.BlogpostRepository;
import com.rayaan.blogpost.resource.model.BlogRepresentation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresBlogpostRepository implements BlogpostRepository {
    Connection connection;
    Statement stmt;
    KafkaBlogpostEventProducer kafkaBlogpostEventProducer;
    private static final Logger LOGGER = Logger.getLogger(PostgresBlogpostRepository.class.getName());
    public PostgresBlogpostRepository(PostgresInitialSetup postgresInitialSetup, AdditionalConfig additionalConfig){
        connection = postgresInitialSetup.getConnection();
        stmt=postgresInitialSetup.getStatement();
        postgresInitialSetup.createTable();
        //postgresInitialSetup.deleteTable();
        kafkaBlogpostEventProducer = new KafkaBlogpostEventProducer(additionalConfig);
        //kafkaBlogpostEventProducer.createProducer();
    }
    public Boolean addBlog(BlogRepresentation blogRepresentation) {
        try{
            String sql = "INSERT INTO BLOG (blogId,createdBy,blogTopic,headline,blogText,image) "
                    + "VALUES ('"+blogRepresentation.getBlogId()+"', '"+
                    blogRepresentation.getCreatedBy()+"', '"+
                    blogRepresentation.getBlogTopic()+"', '"+
                    blogRepresentation.getHeadline()+"', '"+
                    blogRepresentation.getBlogText()+"','"+
                    blogRepresentation.getImage()+"' );";
            stmt.executeUpdate(sql);
            kafkaBlogpostEventProducer.produce(blogRepresentation.getBlogId(),"Blog Created Event");
            return true;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public BlogRepresentation getBlog(String blogID) {

        try{
            String sql = "SELECT * FROM BLOG WHERE blogId = '"+blogID+"';";
            ResultSet rs = stmt.executeQuery(sql);
            BlogRepresentation blog = new BlogRepresentation();
            rs.next();
            blog.setBlogId(rs.getString("blogId"));
            blog.setCreatedBy(rs.getString("createdBy"));
            blog.setBlogTopic(rs.getString("blogTopic"));
            blog.setHeadline(rs.getString("headline"));
            blog.setBlogText(rs.getString("blogText"));
            blog.setImage(rs.getString("image"));
            kafkaBlogpostEventProducer.produce(blog.getBlogId(),"Blog Read Event");
            return blog;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<BlogRepresentation> getAll() {
        try{
            List<BlogRepresentation> blogs =  new ArrayList<>();
            String sql = "SELECT * FROM BLOG;";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                BlogRepresentation blog = new BlogRepresentation();
                blog.setBlogId(rs.getString("blogId"));
                blog.setCreatedBy(rs.getString("createdBy"));
                blog.setBlogTopic(rs.getString("blogTopic"));
                blog.setHeadline(rs.getString("headline"));
                blog.setBlogText(rs.getString("blogText"));
                blog.setImage(rs.getString("image"));
                blogs.add(blog);
            }
            kafkaBlogpostEventProducer.produce("No Blog ID","All Blogs Read Event");
            return blogs;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Boolean deleteBlog(String blogID) {
        try{
            String sql = "DELETE FROM BLOG WHERE blogId = '"+blogID+"';";
            stmt.executeUpdate(sql);
            kafkaBlogpostEventProducer.produce(blogID,"Blog Deleted Event");
            return true;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, null, e);
            return false;
        }
    }

    public Boolean updateBlog(BlogRepresentation blogRepresentation) {
        try{
            String sql = "UPDATE BLOG SET " +
                    "createdBy = '"+blogRepresentation.getCreatedBy()+"'," +
                    "blogTopic = '"+blogRepresentation.getBlogTopic()+"'," +
                    "headline = '"+blogRepresentation.getHeadline()+"'," +
                    "blogText = '"+blogRepresentation.getBlogText()+"'," +
                    "image = '"+blogRepresentation.getImage()+"'" +
                    " WHERE blogId = '"+blogRepresentation.getBlogId()+"';";
            stmt.executeUpdate(sql);
            kafkaBlogpostEventProducer.produce(blogRepresentation.getBlogId(),"Blog Updated Event");
            return true;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, null, e);
            return false;
        }
    }
}
