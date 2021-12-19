package com.uts_pbp_d_9229.dao;

public class City {
    private int id;
    private String nama, picture;

    public City (int id, String nama, String picture){
        this.id = id;
        this.nama = nama;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
