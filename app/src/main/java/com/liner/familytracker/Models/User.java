package com.liner.familytracker.Models;

import java.util.Random;

public class User {
    private String userName;
    private String userPhoneNumber;
    private String userLastOnline;
    private String userPhotoUrl;
    private String userPassword;
    private String userEmail;
    private String userInviteCode;
    private String userUID;
    private String deviceToken;
    private boolean isOnline = false;

    public User(){}

    public User(String userName, String userPhoneNumber, String userLastOnline, String userPhotoUrl, String userPassword, String userEmail, String userInviteCode, String userUID, String deviceToken, boolean isOnline) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userLastOnline = userLastOnline;
        this.userPhotoUrl = userPhotoUrl;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userInviteCode = userInviteCode;
        this.userUID = userUID;
        this.deviceToken = deviceToken;
        this.isOnline = isOnline;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }


    public String getUserLastOnline() {
        return userLastOnline;
    }

    public void setUserLastOnline(String userLastOnline) {
        this.userLastOnline = userLastOnline;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserInviteCode() {
        return userInviteCode;
    }

    public void setUserInviteCode(String userInviteCode) {
        this.userInviteCode = userInviteCode;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public static String generateInviteCode(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
