package com.domyos.econnected.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.domyos.econnected.enity.CheckInCallBackInfo;
import com.domyos.econnected.enity.RaceCheckInfo;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;


public class TestFragment extends BaseFragment {

    @BindView(R.id.text_text)
    TextView textView;
    CheckInCallBackInfo.RaceCheckOutInfo  info;
    private List<CheckInCallBackInfo.RaceCheckOutInfo> list;
    private String text = "{\"data\":[{\"deviceType\":0,\"distance\":1.6,\"time \":\"2625\",\"calorie\":89,\"picId\":103,\"username\":\"500\"}],\"msgId\":30004,\"info\":\"OK\"}";
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initSomething() {


        try {
            text.trim();
            JSONObject jsonObject = new JSONObject(text);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String name = object.getString("username");
                int calorie = object.optInt("calorie");
                String time = object.optString("time");
                Log.e("1", "name：" + name + "  calorie：" + calorie + "  time：" + time);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        Gson g = new Gson();
        CheckInCallBackInfo s = g.fromJson(text, CheckInCallBackInfo.class);
        list = s.getData();
        LogUtils.e("-----initSomething--------");
        for (int i =0;i<list.size();i++){
            LogUtils.e("-----initSomething-----for---"+list.get(i).toString());
        }

    }

}
