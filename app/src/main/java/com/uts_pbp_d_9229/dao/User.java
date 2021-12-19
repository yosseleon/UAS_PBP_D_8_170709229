package com.uts_pbp_d_9229.dao;

public class User {
    private String userId, name, email, noHp, address, foto, password;

    public User(String userId, String name, String email, String noHp, String address, String foto, String password){
        this.userId = userId;
        this.name = name;
        this.email =email;
        this.noHp = noHp;
        this.address = address;
        this.foto = foto;
        this.password =password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
