package com.domyos.econnected.biz.listener;


import com.domyos.econnected.enity.MonthInfo;

public interface MonthDataCallBack {
    void getMonthDataSuccess(boolean monthData, MonthInfo.MonthData[] mMonthData);
    void getMonthDataFailed(String msg);
}
