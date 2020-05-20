package com.domyos.econnected.event;

import com.domyos.econnected.enity.UIEventInfo;

public class UIEvent {

    public int action;
    public UIEventInfo eventInfo;

    public UIEvent(int action) {
        this.action = action;
    }

    public UIEvent(int action, UIEventInfo eventInfo) {
        this.action = action;
        this.eventInfo = eventInfo;
    }

    //返回首页
    public static final int ACTION_BACK_HOMEPAGE = 1;
    //设备界面
    public static final int ACTION_SPROT_EQUIPMENT = 2;
    //设备连接成功
    public static final int ACTION_EQUIPMENT_NAME = 3;
    //显示tab
    public static final int ACTION_SHOW_TAB = 4;
    //隐藏tab
    public static final int ACTION_HIDE_TAB = 5;
    //报告页面
    public static final int ACTION_REPORT_FRAGMENT = 6;
    //登录成功
    public static final int ACTION_LOGIN_FRAGMENT = 7;
    //运动界面跳转
    public static final int ACTION_RUN_EQUIPMENT = 8;
    //课程界面
    public static final int ACTION_CURR_FRAGMENT = 9;
    //视频播放界面
    public static final int ACTION_VIDEO_FRAGMENT = 10;
    //登录成功
    public static final int ACTION_SHOW_LOGINFRAGMENT = 11;
    //设置年龄
    public static final int ACTION_AGE_FRAGMENT = 12;
    //设置身高
    public static final int ACTION_HEIGHT_FRAGMENT = 13;
    //设置体重
    public static final int ACTION_WEIGHT_FRAGMENT = 14;
    //设置着装
    public static final int ACTION_CLOTHES_FRAGMENT = 15;
    //设置着装
    public static final int ACTION_REGISTER_FRAGMENT = 16;

}
