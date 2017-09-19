package com.rulzurlibrary.common;


public class User {
    private static User instance = null;
    private String tokenId;
    private String mail;

    private User() {}

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public boolean isAuthenticated() {
        return tokenId != null && mail != null;
    }

    public void Authenticate(String tokenId, String mail) {
        this.tokenId = tokenId;
        this.mail = mail;
    }
}
