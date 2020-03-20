package com.liner.familytracker.DatabaseModels;

public class PlaceModel {
    private String placeName;
    private String longtitude;
    private String latitude;
    private String placeRadius;

    public PlaceModel(){}

    public PlaceModel(String placeName, String longtitude, String latitude, String placeRadius) {
        this.placeName = placeName;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.placeRadius = placeRadius;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getPlaceRadius() {
        return placeRadius;
    }

    public void setPlaceRadius(String placeRadius) {
        this.placeRadius = placeRadius;
    }
}
