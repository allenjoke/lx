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
import com.domyos.econnected.biz.listener.WeekDataCallBack;
import com.domyos.econnected.enity.WeekInfo;
import com.domyos.econnected.ui.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;


public class WeekFragment extends BaseFragment implements WeekDataCallBack {

    @BindView(R.id.week_distance)
    TextView week_distance;
    @BindView(R.id.week_kcal)
    TextView week_kcal;
    @BindView(R.id.week_time)
    TextView week_time;
    @BindView(R.id.week_speed)
    TextView week_speed;
    @BindView(R.id.week_rpm)
    TextView week_rpm;


    private float distance, speed;
    private int time, rpm, calorie;


    private SportDataBiz sportDataBiz = new SportDataBizImpl();


    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_week;
    }

    @Override
    protected void initSomething() {
        sportDataBiz.getWeekData(this);
    }


    @Override
    public void getWeekDataSuccess(boolean weekInfo, WeekInfo.WeekData[] mWeekInfo) {

        for (int i = 0; i < mWeekInfo.length; i++) {

            distance = +mWeekInfo[i].getOdometer();
            time = +mWeekInfo[i].getElapse();
            calorie = +mWeekInfo[i].getCalorie();
            speed = +mWeekInfo[i].getAvg_speed();
            time = +mWeekInfo[i].getAvg_res();

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                week_distance.setText(distance + "");
                week_kcal.setText(calorie + "");
                week_time.setText(getTime(time) + "");
                if (mWeekInfo.length == 0) {
                    week_rpm.setText(0 + "");
                    week_speed.setText(0 + "");
                } else {
                    double avg_rpm = (double) rpm / (double) mWeekInfo.length;
                    week_rpm.setText(Math.ceil(avg_rpm) + "");

                    double avg_speed = (double) speed / (double) mWeekInfo.length;
                    week_speed.setText(Math.ceil(avg_speed) + "");
                }
            }
        });


    }

    @Override
    public void getWeekDataFailed(String msg) {

    }


    public static String getTime(int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }
}
