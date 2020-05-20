package com.domyos.econnected.biz.listener;

public interface UpLoadCallBack {
    void upLoadSuccess(boolean upLoad,String msg);
    void upLoadFailed(String msg);
}
