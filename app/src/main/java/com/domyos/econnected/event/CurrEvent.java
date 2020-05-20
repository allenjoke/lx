package com.domyos.econnected.event;

public class CurrEvent {

    public int action;
   // public UIEventInfo eventInfo;

    public CurrEvent(int action) {
        this.action = action;
    }

    /*public CurrEvent(int action, UIEventInfo eventInfo) {
        this.action = action;
        this.eventInfo = eventInfo;
    }*/

    //返回首页
    public static final int ACTION_BACK_CURR = 2000;
    //设备界面
    public static final int ACTION_CURR_01 = 2001;
    //设备连接成功
    public static final int ACTION_CURR_02= 2003;
    //显示tab
    public static final int ACTION_CURR_03 = 2004;


}
