package com.domyos.econnected.biz.listener;

import com.domyos.econnected.enity.YearInfo;

public interface YearDataCallBack {
    void getYearDataSuccess(boolean yearData, YearInfo.YearData[] mYearData);
    void getYearDataFailed(String msg);
}
