package com.liner.familytracker.Models;

public class RequestModel {
    private String requestStatus; // 1 - canceled, 2 - added;
    private String requestUID;
    private String requestTime;

    public RequestModel(String requestStatus, String requestUID, String requestTime) {
        this.requestStatus = requestStatus;
        this.requestUID = requestUID;
        this.requestTime = requestTime;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestUID() {
        return requestUID;
    }

    public void setRequestUID(String requestUID) {
        this.requestUID = requestUID;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
