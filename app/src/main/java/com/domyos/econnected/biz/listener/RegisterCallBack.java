package com.domyos.econnected.biz.listener;

public interface RegisterCallBack {


    void registerSuccess(boolean register,String msg);
    void registerFailed(String msg);

}
