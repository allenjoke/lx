package com.domyos.econnected.biz.listener;

public interface LogoutCallBack {
    void logoutSuccess(boolean logout,String msg);
    void logoutFailed(String msg);


}
