package com.domyos.econnected.event;

/**
 * author : SimonWang
 * date : 2019-11-16 19:31
 * description :
 */
public class HeartRateEvent {
    public int heartRate;
    public String action;

    public HeartRateEvent(String action, int heartRate) {
        this.heartRate = heartRate;
        this.action = action;
    }

    public static final String HEART_RATE = "get heart rate";
    public static final int DISCONNTED_HEART_RATE = 22;
}
