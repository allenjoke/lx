package com.domyos.econnected.biz.listener;

public interface ResetPsCallBack {

    void resetPsSuccess(boolean reset,String msg);
    void resetPsFailed(String msg);

}
