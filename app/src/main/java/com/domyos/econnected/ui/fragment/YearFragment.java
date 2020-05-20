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
import com.domyos.econnected.biz.listener.YearDataCallBack;
import com.domyos.econnected.enity.YearInfo;
import com.domyos.econnected.ui.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;


public class YearFragment extends BaseFragment implements YearDataCallBack {
    @BindView(R.id.year_distance)
    TextView year_distance;
    @BindView(R.id.year_kcal)
    TextView year_kcal;
    @BindView(R.id.year_time)
    TextView year_time;
    @BindView(R.id.year_speed)
    TextView year_speed;
    @BindView(R.id.year_rpm)
    TextView year_rpm;
    private SportDataBiz sportDataBiz = new SportDataBizImpl();

    private float distance, speed;
    private int time, rpm, calorie;

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_year;
    }

    @Override
    protected void initSomething() {
        sportDataBiz.getYearData(this);
    }


    @Override
    public void getYearDataSuccess(boolean yearData, YearInfo.YearData[] mYearData) {
        for (int i = 0; i < mYearData.length; i++) {

            distance = +mYearData[i].getOdometer();
            time = +mYearData[i].getElapse();
            calorie = +mYearData[i].getCalorie();
            speed = +mYearData[i].getAvg_speed();
            time = +mYearData[i].getAvg_res();

        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                year_distance.setText(distance + "");
                year_kcal.setText(calorie + "");
                year_time.setText(getTime(time )+ "");
                if (mYearData.length == 0) {
                    year_rpm.setText(0 + "");
                    year_speed.setText(0 + "");

                } else {
                    double avg_rpm = (double) rpm / (double) mYearData.length;
                    year_rpm.setText(Math.ceil(avg_rpm) + "");

                    double avg_speed = (double) speed / (double) mYearData.length;
                    year_speed.setText(Math.ceil(avg_speed) + "");
                }
            }
        });

    }

    @Override
    public void getYearDataFailed(String msg) {

    }

    public static String getTime(int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }
}
