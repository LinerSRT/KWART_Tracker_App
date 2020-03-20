package com.liner.familytracker.Models;

public class Group {
    private String groupName;
    private String groupUID;
    private String[] groupMembers;

    public Group(){
    }

    public Group(String groupName, String groupUID, String[] groupMembers) {
        this.groupName = groupName;
        this.groupUID = groupUID;
        this.groupMembers = groupMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }

    public String[] getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(String[] groupMembers) {
        this.groupMembers = groupMembers;
    }
}
