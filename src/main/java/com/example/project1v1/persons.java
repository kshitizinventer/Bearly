package com.example.project1v1;

import android.net.Uri;

public class persons {

    private String bio;
    private String branch;
    private String imageurl;
    private String name;
    private String year;
    private String id;

    public persons(String bio, String branch, String imageurl, String name, String year, String id) {
        this.bio = bio;
        this.branch = branch;
        this.imageurl = imageurl;
        this.name = name;
        this.year = year;
        this.id = id;
    }

    public persons() {
    }

    public String getBio() {
        return bio;
    }

    public String getBranch() {
        return branch;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getId() {
        return id;
    }
}
