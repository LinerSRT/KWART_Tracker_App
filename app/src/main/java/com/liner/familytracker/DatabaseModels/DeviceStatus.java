package com.liner.familytracker.DatabaseModels;

public class DeviceStatus {
    private int locationAccuracy;
    private int locationAltitude;
    private float locationLat;
    private float locationLon;
    private long locationTime;
    private int locationDirection;
    private int locationSpeed;

    private int batteryVoltage;
    private boolean batteryCharging;
    private float batteryLevelPercent;
    private int batteryTemp;
    private int batteryLevel;

    private boolean isScreenOn;
    private String networkType;
    private int ringerMode;

    public DeviceStatus() {
    }

    public int getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(int locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public int getLocationAltitude() {
        return locationAltitude;
    }

    public void setLocationAltitude(int locationAltitude) {
        this.locationAltitude = locationAltitude;
    }

    public float getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(float locationLat) {
        this.locationLat = locationLat;
    }

    public float getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(float locationLon) {
        this.locationLon = locationLon;
    }

    public long getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(long locationTime) {
        this.locationTime = locationTime;
    }

    public int getLocationDirection() {
        return locationDirection;
    }

    public void setLocationDirection(int locationDirection) {
        this.locationDirection = locationDirection;
    }

    public int getLocationSpeed() {
        return locationSpeed;
    }

    public void setLocationSpeed(int locationSpeed) {
        this.locationSpeed = locationSpeed;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public boolean isBatteryCharging() {
        return batteryCharging;
    }

    public void setBatteryCharging(boolean batteryCharging) {
        this.batteryCharging = batteryCharging;
    }

    public float getBatteryLevelPercent() {
        return batteryLevelPercent;
    }

    public void setBatteryLevelPercent(float batteryLevelPercent) {
        this.batteryLevelPercent = batteryLevelPercent;
    }

    public int getBatteryTemp() {
        return batteryTemp;
    }

    public void setBatteryTemp(int batteryTemp) {
        this.batteryTemp = batteryTemp;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean isScreenOn() {
        return isScreenOn;
    }

    public void setScreenOn(boolean screenOn) {
        isScreenOn = screenOn;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public int getRingerMode() {
        return ringerMode;
    }

    public void setRingerMode(int ringerMode) {
        this.ringerMode = ringerMode;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "locationAccuracy=" + locationAccuracy +
                ", locationAltitude=" + locationAltitude +
                ", locationLat=" + locationLat +
                ", locationLon=" + locationLon +
                ", locationTime=" + locationTime +
                ", locationDirection=" + locationDirection +
                ", locationSpeed=" + locationSpeed +
                ", batteryVoltage=" + batteryVoltage +
                ", batteryCharging=" + batteryCharging +
                ", batteryLevelPercent=" + batteryLevelPercent +
                ", batteryTemp=" + batteryTemp +
                ", batteryLevel=" + batteryLevel +
                ", isScreenOn=" + isScreenOn +
                ", networkType='" + networkType + '\'' +
                ", ringerMode=" + ringerMode +
                '}';
    }
}
