package com.uts_pbp_d_9229.dao;

import java.io.Serializable;

public class Destination implements Serializable {
    private int id;
    private String name, city_name, category, picture, location, description;

    public Destination(int id, String city_name, String name, String category, String picture,
                       String location, String description){
        this.id = id;
        this.city_name = city_name;
        this.name = name;
        this.category = category;
        this.picture = picture;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
