package com.pashikhmin.ismobileapp.model.helpdesk;

import com.pashikhmin.ismobileapp.network.json.JSONField;

import java.io.Serializable;

public class Actor implements Serializable {
    @JSONField
    private int id;
    @JSONField
    private String username;

    @JSONField
    private String givenName;

    @JSONField
    private String familyName;

    @JSONField
    private String email;

    @JSONField
    private String imageSrc;

    public Actor() {
    }

    public Actor(String username) {
        this.username = username;
        this.id = username.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getFullName() {
        return getGivenName() + " " + getFamilyName();
    }
}
