package com.domyos.econnected.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.domyos.econnected.biz.SportDataBiz;
import com.domyos.econnected.biz.iml.SportDataBizImpl;
import com.domyos.econnected.biz.listener.MonthDataCallBack;
import com.domyos.econnected.enity.MonthInfo;
import com.domyos.econnected.ui.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;

public class MonthFragment extends BaseFragment implements MonthDataCallBack {

    @BindView(R.id.month_distance)
    TextView month_distance;
    @BindView(R.id.month_kcal)
    TextView month_kcal;
    @BindView(R.id.month_time)
    TextView month_time;
    @BindView(R.id.month_speed)
    TextView month_speed;
    @BindView(R.id.month_rpm)
    TextView month_rpm;
    private SportDataBiz sportDataBiz = new SportDataBizImpl();

    private float distance, speed;
    private int time, rpm, calorie;

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_month;
    }

    @Override
    protected void initSomething() {
        sportDataBiz.getMonthData(this);
    }
    public static String getTime(int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }

    @Override
    public void getMonthDataSuccess(boolean monthData, MonthInfo.MonthData[] mMonthData) {
        for (int i = 0; i < mMonthData.length; i++) {

            distance = +mMonthData[i].getOdometer();
            time = +mMonthData[i].getElapse();
            calorie = +mMonthData[i].getCalorie();
            speed = +mMonthData[i].getAvg_speed();
            time = +mMonthData[i].getAvg_res();

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                month_distance.setText(distance + "");
                month_kcal.setText(calorie + "");
                month_time.setText(getTime(time )+ "");
                if (mMonthData.length == 0) {
                    month_rpm.setText(0 + "");
                    month_speed.setText(0 + "");

                } else {
                    double avg_rpm = (double) rpm / (double) mMonthData.length;
                    month_rpm.setText(Math.ceil(avg_rpm) + "");

                    double avg_speed = (double) speed / (double) mMonthData.length;
                    month_speed.setText(Math.ceil(avg_speed) + "");
                }
            }
        });
    }

    @Override
    public void getMonthDataFailed(String msg) {

    }
}
