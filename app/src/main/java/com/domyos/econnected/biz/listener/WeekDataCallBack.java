package com.domyos.econnected.biz.listener;

import com.domyos.econnected.enity.WeekInfo;

public interface WeekDataCallBack {
    void getWeekDataSuccess(boolean weekInfo, WeekInfo.WeekData[] mWeekInfo);
    void getWeekDataFailed(String msg);
}
