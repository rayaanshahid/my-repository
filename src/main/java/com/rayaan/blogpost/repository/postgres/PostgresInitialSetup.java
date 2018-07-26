package com.rayaan.blogpost.repository.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresInitialSetup {
    Connection connection;
    Statement stmt;
    private static final Logger LOGGER = Logger.getLogger(PostgresInitialSetup.class.getName());
    public PostgresInitialSetup(){

    }
    Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5433/myDB",
                            "dbuser", "myblogs");
            System.out.println("Opened database successfully");
            return connection;
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, null, e);
            System.out.println("NOT Opened database successfully");
            return null;
        }
    }

    Statement getStatement(){
        try{
            stmt= connection.createStatement();
            return stmt;
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, null, e);
            return null;
        }
    }

    void createTable(){
        try{
            String sql = "CREATE TABLE BLOG " +
                    "(blogId TEXT PRIMARY KEY     NOT NULL," +
                    " createdBy           TEXT    NOT NULL, " +
                    " blogTopic            TEXT     NOT NULL, " +
                    " headline        TEXT, " +
                    " blogText         TEXT     NOT NULL,"+
                    " image          TEXT   NOT NULL )";
            stmt.executeUpdate(sql);
            System.out.println("Created table successfully");
        }catch(Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            System.out.println("NOT created table successfully");
        }
    }

    void deleteTable(){
        try{
            String sql = "DROP TABLE BLOG";
            stmt.executeUpdate(sql);
            System.out.println("Deleted table successfully");
        }catch(Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            System.out.println("NOT Deleted table successfully");
        }
    }

    void closeConnection() throws Exception{
        connection.close();
    }
}
