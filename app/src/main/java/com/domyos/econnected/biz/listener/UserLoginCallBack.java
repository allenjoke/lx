package com.domyos.econnected.biz.listener;


import com.domyos.econnected.enity.UserInfoData;

public interface UserLoginCallBack {

    void loginSuccess(boolean login , String msg, UserInfoData userInfoData);
    void loginFailed(String msg);

}
