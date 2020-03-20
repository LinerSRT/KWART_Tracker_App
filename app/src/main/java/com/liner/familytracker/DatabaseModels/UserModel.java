package com.liner.familytracker.DatabaseModels;

import java.util.List;
import java.util.Random;

public class UserModel {
    private String UID;
    private String phoneNumber;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String longtitude;
    private String latitude;
    private String inviteCode;
    private String photoUrl;
    private String deviceToken;
    private String registerFinished;
    private List<UserJoinedCircles> joinedCircles;


    public UserModel() {
    }

    public String isRegisterFinished() {
        return registerFinished;
    }

    public void setRegisterFinished(String registerFinished) {
        this.registerFinished = registerFinished;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public List<UserJoinedCircles> getJoinedCircles() {
        return joinedCircles;
    }

    public void setJoinedCircles(List<UserJoinedCircles> joinedCircles) {
        this.joinedCircles = joinedCircles;
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
