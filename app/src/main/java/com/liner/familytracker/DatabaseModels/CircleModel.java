package com.liner.familytracker.DatabaseModels;

import java.util.List;
import java.util.Random;

public class CircleModel {
    private String circleName;
    private List<UserModel> joinedUsers;
    private String cricleUID;
    private List<PlaceModel> circlePlaces;

    public CircleModel() {
    }

    public CircleModel(String circleName, List<UserModel> joinedUsers, String cricleUID, List<PlaceModel> circlePlaces) {
        this.circleName = circleName;
        this.joinedUsers = joinedUsers;
        this.cricleUID = cricleUID;
        this.circlePlaces = circlePlaces;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public List<UserModel> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(List<UserModel> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public String getCricleUID() {
        return cricleUID;
    }

    public void setCricleUID(String cricleUID) {
        this.cricleUID = cricleUID;
    }

    public List<PlaceModel> getCirclePlaces() {
        return circlePlaces;
    }

    public void setCirclePlaces(List<PlaceModel> circlePlaces) {
        this.circlePlaces = circlePlaces;
    }

    public static String generateCircleUID(){
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
