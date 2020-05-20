package com.domyos.econnected.event;

public class UserEditEvent {

    public int action;

    public UserEditEvent(int action) {
        this.action = action;
    }



    //生日设置
    public static final int ACTION_EDIT_AGE = 3001;
    //身高设置
    public static final int ACTION_EDIT_HEIGHT = 3002;
    //体重设置
    public static final int ACTION_EDIT_WEIGHT= 3003;
    //肖像设置
    public static final int ACTION_EDIT_PIC = 3004;
    //年龄设置返回
    public static final int ACTION_AGE_BACK = 3005;
    //身高设置返回
    public static final int ACTION_HEIGHT_BACK = 3006;
    //体重设置返回
    public static final int ACTION_WEIGHT_BACK= 3007;
    //肖像设置返回
    public static final int ACTION_PIC_BACK = 3008;


}
