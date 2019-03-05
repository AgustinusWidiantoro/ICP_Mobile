package com.example.agustinuswidiantoro.icp_mobile.model;

import java.io.Serializable;

public class DataBook implements Serializable {

    String id;
    String createAt;
    String fullname;
    String name_book;
    String description_book;

    public DataBook(String id, String createAt, String fullname, String name_book, String description_book) {
        this.id = id;
        this.createAt = createAt;
        this.fullname = fullname;
        this.name_book = name_book;
        this.description_book = description_book;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getName_book() {
        return name_book;
    }

    public void setName_book(String name_book) {
        this.name_book = name_book;
    }

    public String getDescription_book() {
        return description_book;
    }

    public void setDescription_book(String description_book) {
        this.description_book = description_book;
    }
}
