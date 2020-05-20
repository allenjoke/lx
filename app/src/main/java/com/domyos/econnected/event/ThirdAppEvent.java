package com.domyos.econnected.event;

public class ThirdAppEvent {

    public int action;

    public ThirdAppEvent(int action) {
        this.action = action;
    }

    //Netflix
    public static final int ACTION_NETFLIX = 900;
    //hulu
    public static final int ACTION_HULU = 901;
    //amazon price
    public static final int ACTION_AMAZON = 902;
    //youtube
    public static final int ACTION_YOUTUBE = 903;
}
