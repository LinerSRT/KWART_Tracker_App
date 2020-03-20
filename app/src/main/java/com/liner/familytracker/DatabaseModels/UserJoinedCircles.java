package com.liner.familytracker.DatabaseModels;

public class UserJoinedCircles {
    private String circleUID;
    private String circleName;

    public UserJoinedCircles(){}

    public UserJoinedCircles(String circleUID, String circleName) {
        this.circleUID = circleUID;
        this.circleName = circleName;
    }

    public String getCircleUID() {
        return circleUID;
    }

    public void setCircleUID(String circleUID) {
        this.circleUID = circleUID;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
}
