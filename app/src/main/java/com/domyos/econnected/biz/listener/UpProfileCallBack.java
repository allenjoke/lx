package com.domyos.econnected.biz.listener;

public interface UpProfileCallBack {

    void UpProfileSuccess(boolean profile,String msg);
    void UpProfileFailed(String msg);
}
