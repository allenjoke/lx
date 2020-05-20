package com.domyos.econnected.biz;

import com.domyos.econnected.biz.listener.MonthDataCallBack;
import com.domyos.econnected.biz.listener.StatCallBack;
import com.domyos.econnected.biz.listener.UpLoadCallBack;
import com.domyos.econnected.biz.listener.WeekDataCallBack;
import com.domyos.econnected.biz.listener.YearDataCallBack;
import com.domyos.econnected.enity.SportDataInfo;

public interface SportDataBiz {
    //上传数据
    void upLoadSportData(SportDataInfo sportDataInfo,UpLoadCallBack upLoadCallBack);
    //获取周、月、年的总数据（总时间，总卡路里，总里程，总平均心率）
    //统计
    void getStat(StatCallBack statCallBack);
    //周详
    void getWeekData(WeekDataCallBack weekDataCallBack);
    //月详
    void getMonthData(MonthDataCallBack monthDataCallBack);
    //年详
    void getYearData(YearDataCallBack yearDataCallBack);

}
