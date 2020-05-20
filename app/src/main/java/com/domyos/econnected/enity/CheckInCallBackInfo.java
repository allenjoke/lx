package com.domyos.econnected.enity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckInCallBackInfo {

    private int msgId;
    private String info;
    private List<RaceCheckOutInfo> data;

    public List<RaceCheckOutInfo> getData() {
        return data;
    }

    public void setData(List<RaceCheckOutInfo> data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public static class RaceCheckOutInfo {
        private String username;
        @SerializedName("time")
        private long time;
        private int picId;
        private float distance;
        private int calorie;
        private int deviceType;

        public RaceCheckOutInfo(){

        }

        public RaceCheckOutInfo(String username, long time, int picId, float distance, int calorie, int deviceType) {
            this.username = username;
            this.time = time;
            this.picId = picId;
            this.distance = distance;
            this.calorie = calorie;
            this.deviceType = deviceType;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        @Override
        public String toString() {
            return "RaceCheckOutInfo{" +
                    "username='" + username + '\'' +
                    ", time='" + time + '\'' +
                    ", picId=" + picId +
                    ", distance=" + distance +
                    ", calorie=" + calorie +
                    ", deviceType=" + deviceType +
                    '}';
        }
    }




}
