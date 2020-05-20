package com.domyos.econnected.enity;

import com.google.gson.JsonObject;

public class RaceCheckInfo {

    private int msgId;
    private int roomId;
    private int userId;
    private CheckInfo data;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CheckInfo getData() {
        return data;
    }

    public void setData(CheckInfo data) {
        this.data = data;
    }

    public static class CheckInfo{

        private float distance;
        private String time;
        private int calorie;
        private int rpm;
        private int heartrate;
        private float incline;
        private float speed;
        private int deviceType;

        public CheckInfo() {

        }

        public CheckInfo(float distance, String time, int calorie, int rpm, int heartrate, float incline, float speed, int deviceType) {
            this.distance = distance;
            this.time = time;
            this.calorie = calorie;
            this.rpm = rpm;
            this.heartrate = heartrate;
            this.incline = incline;
            this.speed = speed;
            this.deviceType = deviceType;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public int getRpm() {
            return rpm;
        }

        public void setRpm(int rpm) {
            this.rpm = rpm;
        }

        public int getHeartrate() {
            return heartrate;
        }

        public void setHeartrate(int heartrate) {
            this.heartrate = heartrate;
        }

        public float getIncline() {
            return incline;
        }

        public void setIncline(float incline) {
            this.incline = incline;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }
    }


}
