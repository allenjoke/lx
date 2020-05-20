package com.domyos.econnected.enity;

public class UserInfoViewPagerData {

    private int distance;
    private int time;
    private int kcal;
    private int speed;
    private int rpm;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    @Override
    public String toString() {
        return "UserInfoViewPagerData{" +
                "distance=" + distance +
                ", time=" + time +
                ", kcal=" + kcal +
                ", speed=" + speed +
                ", rpm=" + rpm +
                '}';
    }
}
