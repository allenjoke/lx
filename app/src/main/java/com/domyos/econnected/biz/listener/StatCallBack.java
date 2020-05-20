package com.domyos.econnected.biz.listener;

import com.domyos.econnected.enity.StatInfo;

public interface StatCallBack {

    void getStatSuccess(boolean exec, StatInfo statInfo);
    void getStatFailed(String msg);
}
